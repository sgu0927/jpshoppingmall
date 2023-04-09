package com.jpshoppingmall.order.config;

import com.jpshoppingmall.domain.payment.dto.PaymentSuccessDto;
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
    public ConsumerFactory<String, PaymentSuccessDto> paymentSuccessDtoConsumerFactory() {
        JsonDeserializer<PaymentSuccessDto> deserializer = new JsonDeserializer<>(PaymentSuccessDto.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "order-create");

        return new DefaultKafkaConsumerFactory<>(
            configs,
            new StringDeserializer(),
            deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentSuccessDto> kafkaPaymentSuccessDtoListener() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentSuccessDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentSuccessDtoConsumerFactory());
        return factory;
    }
}
