package com.jpshoppingmall.purchase.config;

import com.jpshoppingmall.domain.product.dto.ProductCountDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public ConsumerFactory<String, ProductCountDto> paymentSuccessDtoConsumerFactory() {
        JsonDeserializer<ProductCountDto> deserializer = new JsonDeserializer<>(
            ProductCountDto.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "purchase");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(
            configs,
            new StringDeserializer(),
            deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductCountDto> kafkaProductCountDtoListener() {
        ConcurrentKafkaListenerContainerFactory<String, ProductCountDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentSuccessDtoConsumerFactory());
        return factory;
    }
}
