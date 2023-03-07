package com.shawn.minio;

import com.shawn.minio.properties.MinIOProperties;
import com.shawn.minio.template.MinIOTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author shawn
 * @date 2023年 01月 07日 16:12
 */
@EnableConfigurationProperties(value = {MinIOProperties.class})
public class MinIOAutoConfiguration {

    @Bean
    public MinIOTemplate minIOTemplate(MinIOProperties minIOProperties){
        return new MinIOTemplate(minIOProperties);
    }
}
