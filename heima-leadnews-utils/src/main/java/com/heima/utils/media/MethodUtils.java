package com.heima.utils.media;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shawn
 * @date 2023年 01月 11日 17:47
 */

public class MethodUtils {
    public static List<String> getImages(String content) {
        List<String> urls = new ArrayList<>();
        if (StringUtils.isNotEmpty(content)){
            List<Map> maps = JSONArray.parseArray(content, Map.class);
            for (Map map : maps) {
                String type =(String) map.get("type");
                if ("image".equals(type)){
                    String image = (String) map.get("value");
                    urls.add(image);
                }
            }
        }
        return urls;
    }
}
