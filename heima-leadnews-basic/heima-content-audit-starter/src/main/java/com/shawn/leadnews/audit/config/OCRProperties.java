package com.shawn.leadnews.audit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shawn
 * @date 2023年 01月 14日 20:13
 */
@Data
@ConfigurationProperties(prefix = "ocr")
public class OCRProperties {
    private String dataPath;
    private String language;
}
