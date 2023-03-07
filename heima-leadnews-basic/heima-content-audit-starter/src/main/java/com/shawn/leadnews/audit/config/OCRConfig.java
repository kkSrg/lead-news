package com.shawn.leadnews.audit.config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shawn
 * @date 2023年 01月 15日 18:30
 */
@Configuration
@EnableConfigurationProperties(value = OCRProperties.class)
public class OCRConfig {

    @Bean
    public ITesseract iTesseract(OCRProperties properties){
        ITesseract iTesseract = new Tesseract();
        iTesseract.setLanguage(properties.getLanguage());
        iTesseract.setDatapath(properties.getDataPath());
        return iTesseract;
    }
}
