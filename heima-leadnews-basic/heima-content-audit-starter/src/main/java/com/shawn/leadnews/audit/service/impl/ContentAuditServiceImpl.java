package com.shawn.leadnews.audit.service.impl;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.shawn.leadnews.audit.config.OCRConfig;
import com.shawn.leadnews.audit.result.AuditResult;
import com.baidu.aip.contentcensor.EImgType;
import com.shawn.leadnews.audit.config.AuditConfig;
import com.shawn.leadnews.audit.service.ContentAuditService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author shawn
 * @date 2023年 01月 10日 19:59
 */
@Import(value = {AuditConfig.class, OCRConfig.class})
public class ContentAuditServiceImpl implements ContentAuditService {

    @Resource
    private AipContentCensor client;

    @Resource
    private ITesseract iTesseract;



    /**
     * 文本审核
     *
     * @param content 内容
     */
    public AuditResult auditText(String content){
        JSONObject jsonObject = client.textCensorUserDefined(content);
        return dealResult(jsonObject);
    }

    /**
     * 审核结果处理
     *
     * @param jsonObject json对象
     * @return {@link AuditResult}
     */

    private AuditResult dealResult(JSONObject jsonObject) {
        String conclusion = (String) jsonObject.get("conclusion");
        Integer conclusionType = (Integer) jsonObject.get("conclusionType");
        String msg = conclusion;
        try {
            JSONArray jsonArray = (JSONArray)jsonObject.get("data");
            List<String> messages = new ArrayList<>();
            for (Object next : jsonArray) {
                Map map = com.alibaba.fastjson.JSONArray.parseObject(next.toString(), Map.class);
                if (Objects.nonNull(map.get("msg"))) {
                    messages.add((String) map.get("msg"));
                }
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < messages.size(); i++) {
                if (i!=messages.size()-1){
                    builder.append(messages.get(i)).append(",");
                }else {
                    builder.append(messages.get(i));
                }
            }
            msg = builder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return new AuditResult(conclusionType,conclusion,msg);
    }

    /**
     * 图片url审核
     *
     * @param imageUrl 图像url
     */
    public AuditResult auditImage(String imageUrl){
        JSONObject jsonObject = client.imageCensorUserDefined(imageUrl, EImgType.URL, null);
        return dealResult(jsonObject);
    }


    public AuditResult auditImage(byte[] imageByte){
        JSONObject jsonObject = client.imageCensorUserDefined(imageByte,null);
        return dealResult(jsonObject);
    }


    /**
     * 批量审核图片
     *
     * @param images 图片
     * @return {@link AuditResult}
     */
    public AuditResult auditImage(List<byte[]> images){
        images = images.stream().distinct().collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        for (byte[] image : images) {
             jsonObject = client.imageCensorUserDefined(image,null);
            Integer conclusionType = (Integer) jsonObject.get("conclusionType");
            if ( conclusionType == 4){
                throw new RuntimeException("服务调用错误!");
            }else if (conclusionType == 3 ){
                return dealResult(jsonObject);
            }
        }
        return  dealResult(jsonObject);
    }

    @Override
    public String OCRScan(BufferedImage bufferedImage) {
        try {
            return iTesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

}
