package com.jpshoppingmall.notification.service;

import com.jpshoppingmall.domain.notification.dto.NotificationCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "jpshoppingmall.notification.event", containerFactory = "kafkaNotificationEventListener")
    public void receiveNotificationEvent(NotificationCreateEvent notificationCreateEvent) {
        log.info("[notification-server] NotificationEvent 수신 완료. notificationEvent :: {}",
            notificationCreateEvent.toString());

        notificationService.send(notificationCreateEvent);
    }
}
