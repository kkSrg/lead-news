package com.shawn.leadnews.audit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shawn
 * @date 2023年 01月 10日 19:45
 */
@Data
@ConfigurationProperties(prefix = "baidu.audit")
public class AuditConfigProperties {
    private String  appId;
    private String  apiKey;
    private String  secretKey;
}
