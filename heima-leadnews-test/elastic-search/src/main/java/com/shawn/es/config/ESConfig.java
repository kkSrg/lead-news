package com.shawn.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shawn
 * @date 2023年 01月 26日 19:26
 */
@Configuration
public class ESConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;

    @Bean
    public RestHighLevelClient highLevelClient(){
    return new RestHighLevelClient(RestClient.builder(new HttpHost(host,port,"http")));
    }

}
