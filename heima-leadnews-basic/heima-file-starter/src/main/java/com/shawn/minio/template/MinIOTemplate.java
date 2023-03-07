package com.shawn.minio.template;


import com.shawn.minio.properties.MinIOProperties;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.Strings;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;

/**
 * @author shawn
 * @date 2023年 01月 07日 16:23
 */
@Slf4j
public class MinIOTemplate implements FileStorageService{
    private MinioClient client;
    private MinIOProperties minIOProperties;

    private final static String separator = "/";
    private final static String fileStoreFormat = "yyyy/MM/dd";
    public MinIOTemplate(MinIOProperties minIOProperties){
        this.client= MinioClient.builder().credentials(minIOProperties.getAccessKey(), minIOProperties.getSecretKey()).endpoint(minIOProperties.getEndpoint()).build();
        this.minIOProperties=minIOProperties;
    }

    
    public String builderFilePath(String prefix, String filename){
       StringBuilder stringBuilder = new StringBuilder(50);
       if (Strings.isNotEmpty(prefix)){
           stringBuilder.append(prefix);
       }
        SimpleDateFormat format = new SimpleDateFormat(fileStoreFormat);
       stringBuilder.append(format.format(System.currentTimeMillis())).append(separator).append(filename);
       return stringBuilder.toString();
    }



    /**
     * 上传img文件
     *
     * @param prefix      前缀
     * @param filename    文件名
     * @param inputStream 输入流
     * @return {@link String}
     */
    @Override
    public String uploadImgFile(String prefix, String filename, InputStream inputStream) {
        String storePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(storePath)
                    .contentType("image/jpg")
                    .bucket(minIOProperties.getBucket()).stream(inputStream,inputStream.available(),-1)
                    .build();
            ObjectWriteResponse response = client.putObject(putObjectArgs);
            log.info("minIO返回值:{}",response);
            String url = minIOProperties.getReadPath() + separator + minIOProperties.getBucket() + separator + storePath;
            log.error("图片存储路径为:{}",url);
            return url;
        } catch (Exception e) {
            log.info("图片上传失败!");
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传html文件
     *
     * @param prefix      前缀
     * @param filename    文件名
     * @param inputStream 输入流
     * @return {@link String}
     */
    @Override
    public String uploadHtmlFile(String prefix, String filename, InputStream inputStream) {
        String storePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(storePath)
                    .contentType("text/html")
                    .bucket(minIOProperties.getBucket()).stream(inputStream,inputStream.available(),-1)
                    .build();
            ObjectWriteResponse response = client.putObject(putObjectArgs);
            log.info("minIO返回值:{}",response);
            return minIOProperties.getReadPath() + separator + minIOProperties.getBucket() + separator + storePath;
        } catch (Exception e) {
            log.info("html上传失败!");
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文件
     *
     * @param pathUrl 路径全路径
     */
    @Override
    public void delete(String pathUrl) {
        //处理全路径:http://192.168.200.130:9000/leadnews/2023/01/07/demo
        if (Strings.isEmpty(pathUrl)){
            throw new RuntimeException("文件路径为空!");
        }
        String innerPath = pathUrl.replace(minIOProperties.getReadPath() + separator, "");
        int index = innerPath.indexOf(separator);
        String bucket = innerPath.substring(0, index);
        String storePath = innerPath.substring(index + 1);
        RemoveObjectArgs objectArgs =RemoveObjectArgs.builder()
                .object(storePath)
                .bucket(bucket)
                .build();
        try {
            client.removeObject(objectArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件
     *
     * @param pathUrl 路径url
     * @return {@link byte[]}
     */
    @Override
    public byte[] downLoadFile(String pathUrl) {
        if (Strings.isEmpty(pathUrl)){
            throw new RuntimeException("文件路径为空!");
        }
        InputStream inputStream = null;
        String innerPath = pathUrl.replace(minIOProperties.getReadPath() + separator, "");
        int index = innerPath.indexOf(separator);
        String bucket = innerPath.substring(0, index);
        String storePath = innerPath.substring(index + 1);
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .object(storePath)
                .bucket(bucket).build();
        try {
             inputStream = client.getObject(objectArgs);

        } catch (Exception e) {
            log.error("minio文件下载失败.  文件路径:{}",pathUrl);
            throw new RuntimeException(e);
        }

        byte[] bytes = new byte[100];
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        while (true) {
            int count = 0;
            try {
                count = inputStream.read(bytes, 0, 100);
            } catch (IOException e) {
                log.info("文件下载IO异常");
                throw new RuntimeException(e);
            }

            if (count<=0){
                break;
            }
            //储存数据
            bs.write(bytes,0,count);
        }
        return bs.toByteArray();
    }

}
