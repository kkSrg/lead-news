package com.shawn.media;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.shawn.feign.apis.ArticleFeignClient;
import com.shawn.feign.apis.ScheduleClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author shawn
 * @date 2023年 01月 06日 10:48
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan(value = "com.shawn.media.mapper")
@EnableAsync
@EnableFeignClients(clients = {ArticleFeignClient.class, ScheduleClient.class})
public class WeMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeMediaApplication.class,args);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
