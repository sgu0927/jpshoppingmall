package com.jpshoppingmall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jpshoppingmall.service.RedisMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisMessageListenerContainerConfig {

    private static final String NOTIFICATION_CHANNEL = "topics:notification";

    private final RedisMessageListener redisMessageListener;

    @Value("${spring.redis.notification.host}")
    private String host;
    @Value("${spring.redis.notification.port}")
    private int port;

    @Bean(name = "redisNotificationConnectionFactory")
    public RedisConnectionFactory redisNotificationConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration
            = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModules(new JavaTimeModule(), new Jdk8Module());
//        return mapper;
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
//        GenericJackson2JsonRedisSerializer serializer =
//            new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        // TODO
//        // redisTemplate.setConnectionFactory(redisConnectionFactory(redisClusterConfiguration()));
//        redisTemplate.setConnectionFactory(redisNotificationConnectionFactory());
//
//        // json 형식으로 데이터를 받을 때
//        // 값이 깨지지 않도록 직렬화한다.
//        // 저장할 클래스가 여러개일 경우 범용 JacksonSerializer인 GenericJackson2JsonRedisSerializer를 이용한다
//        // 참고 https://somoly.tistory.com/134
//        // setKeySerializer, setValueSerializer 설정해주는 이유는 RedisTemplate를 사용할 때 Spring - Redis 간 데이터 직렬화, 역직렬화 시 사용하는 방식이 Jdk 직렬화 방식이기 때문입니다.
//        // 동작에는 문제가 없지만 redis-cli을 통해 직접 데이터를 보려고 할 때 알아볼 수 없는 형태로 출력되기 때문에 적용한 설정입니다.
//        // 참고 https://wildeveloperetrain.tistory.com/32
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(serializer);
//        redisTemplate.setEnableTransactionSupport(true); // transaction 허용
//
//        return redisTemplate;
//    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(NOTIFICATION_CHANNEL);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
        @Qualifier("redisNotificationConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisMessageListener, topic());
        log.info("RedisMessageListenerContainer init");
        return container;
    }

}
