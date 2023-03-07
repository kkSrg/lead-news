package com.shawn.leadnews.audit.config;

import com.baidu.aip.contentcensor.AipContentCensor;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author shawn
 * @date 2023年 01月 10日 19:51
 */
@Configuration
@EnableConfigurationProperties(value = AuditConfigProperties.class)
public class AuditConfig {

    @Bean
    public  AipContentCensor aipContentCensor(AuditConfigProperties properties) {
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(properties.getAppId(),properties.getApiKey(), properties.getSecretKey());

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;

    }
}

