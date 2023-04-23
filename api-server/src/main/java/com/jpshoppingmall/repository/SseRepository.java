package com.jpshoppingmall.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseRepository {

    void put(String key, SseEmitter sseEmitter);

    Optional<SseEmitter> get(String key);

    List<String> getKeyListByKeyPrefix(String keyPrefix);

    void remove(String key);
}
