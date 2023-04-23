package com.jpshoppingmall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpshoppingmall.domain.notification.dto.AlertMessage;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.repository.SseRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisMessageListener implements MessageListener {

    private static final String UNDER_SCORE = "_";
    private static final String EVENT_NAME = "sse";
    private final SseRepository sseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Redis Pub/Sub message received: {}", message.toString());

        try {
            AlertMessage alertMessage = objectMapper.readValue(new String(message.getBody()),
                AlertMessage.class);
            log.info("AlertMessage - {}", alertMessage.toString());
            log.info(sseRepository.getKeyListByKeyPrefix(alertMessage.getMemberId().toString())
                .toString());

            sseRepository.getKeyListByKeyPrefix(alertMessage.getMemberId().toString())
                .forEach(key -> {
                    Optional<SseEmitter> emitterOptional = sseRepository.get(key);
                    if (emitterOptional.isPresent()) {
                        SseEmitter emitter = emitterOptional.get();
                        try {
                            emitter.send(SseEmitter.event()
                                .id(getEventId(alertMessage.getMemberId(), LocalDateTime.now()))
                                .name(EVENT_NAME)
                                .data(alertMessage.getContent()));
                            log.info("Emitter Send Complete");
                        } catch (ClientAbortException e) {

                        } catch (IOException e) {
                            sseRepository.remove(key);
                            log.error("SSE send error", e);
                            throw new CommonException(CommonExceptionType.SSE_SEND_ERROR);
                        }
                    }
                });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEventId(Long memberId, LocalDateTime createdDateTime) {
        return memberId.toString() + UNDER_SCORE + createdDateTime.toString();
    }
}
