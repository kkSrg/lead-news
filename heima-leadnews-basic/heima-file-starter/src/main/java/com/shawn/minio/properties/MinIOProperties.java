package com.shawn.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shawn
 * @date 2023年 01月 07日 16:15
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinIOProperties {
    /**
     * minio:
     *   accessKey: minio
     *   secretKey: minio123
     *   bucket: leadnews
     *   endpoint: http://192.168.200.130:9000
     *   readPath: http://192.168.200.130:9000
     */
    private String  accessKey;
    private String  secretKey;
    private String  bucket;
    private String  endpoint;
    private String  readPath;
}
