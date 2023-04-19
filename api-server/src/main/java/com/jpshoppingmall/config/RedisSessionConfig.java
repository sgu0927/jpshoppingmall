package com.jpshoppingmall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisSessionConfig {

    @Value("${spring.redis.session.host}")
    private String host;
    @Value("${spring.redis.session.port}")
    private int port;

    @Primary
    @Bean(name = "redisSessionConnectionFactory")
    public RedisConnectionFactory redisSessionConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration
            = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
