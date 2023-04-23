package com.jpshoppingmall.service;

import com.jpshoppingmall.domain.notification.dto.NotificationCreateEvent;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.repository.SseRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private static final String TOPIC = "jpshoppingmall.notification.event";
//    private static final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;
    private static final String UNDER_SCORE = "_";
    private static final String CONNECTED = "CONNECTED";
    private final SseRepository sseRepository;
    private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplateNotificationEvent;

    public SseEmitter subscribe(Long memberId, LocalDateTime createdDateTime) {
        SseEmitter sse = new SseEmitter(Long.MAX_VALUE);
        String key = getEventId(memberId, createdDateTime);

        sse.onCompletion(() -> {
            log.info("onCompletion callback");
            sseRepository.remove(key);
        });
        sse.onTimeout(() -> {
            log.info("onTimeout callback");
            //만료 시 Repository에서 삭제 되어야함.
            sse.complete();
        });

        sseRepository.put(key, sse);
        try {
            sse.send(SseEmitter.event()
                .name(CONNECTED)
                .id(getEventId(memberId, createdDateTime))
                .data("EventStream Created. [memberId=" + memberId + "]"));
        } catch (IOException exception) {
            sseRepository.remove(key);
            log.info("SSE Exception: {}", exception.getMessage());
            throw new CommonException(CommonExceptionType.SSE_SEND_ERROR);
        }

        // 중간에 연결이 끊겨서 다시 연결할 때, lastEventId를 통해 기존의 받지못한 이벤트를 받을 수 있도록 할 수 있음.
        // 한번의 알림이나 새로고침을 받으면 알림을 slice로 가져오기 때문에
        // 수신 못한 응답을 다시 보내는 로직을 구현하지 않음.
        return sse;
    }

    public void sendCreateEvent(NotificationCreateEvent notificationCreateEvent) {
        log.info(
            "[NotificationService] try produce notificationCreateEvent - notificationCreateEvent :: {}"
            , notificationCreateEvent.toString());

        ListenableFuture<SendResult<String, NotificationCreateEvent>> future = kafkaTemplateNotificationEvent.send(
            TOPIC, notificationCreateEvent);

        future.addCallback(
            new ListenableFutureCallback<SendResult<String, NotificationCreateEvent>>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error("[NotificationService] Unable to send notificationCreateEvent=["
                        + notificationCreateEvent
                        + "] due to : " + ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, NotificationCreateEvent> result) {
                    log.info(
                        "[NotificationService] Sent notificationCreateEvent=["
                            + notificationCreateEvent
                            + "] with offset=[" + result.getRecordMetadata()
                            .offset() + "]");
                }
            });
    }

    private String getEventId(Long userId, LocalDateTime createdDateTime) {
        return userId.toString() + UNDER_SCORE + createdDateTime.toString();
    }
}
