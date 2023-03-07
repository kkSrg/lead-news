package com.shawn.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shawn
 * @date 2023年 01月 26日 16:33
 */
@SpringBootApplication
@MapperScan(value = "com.shawn.es.mapper")
public class ESApplication {
    public static void main(String[] args) {
        SpringApplication.run(ESApplication.class,args);
    }
}
