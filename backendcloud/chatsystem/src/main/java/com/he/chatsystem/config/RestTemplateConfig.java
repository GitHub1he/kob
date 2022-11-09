package com.he.chatsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig { //在spring 之间发送数据
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
