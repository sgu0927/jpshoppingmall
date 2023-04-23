package com.jpshoppingmall.notification.service;

import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.notification.dto.NotificationCreateEvent;
import com.jpshoppingmall.domain.notification.entity.Notification;
import com.jpshoppingmall.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final ChannelTopic channelTopic;
    private final MemberReadService memberReadService;
    private final NotificationRepository notificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void send(NotificationCreateEvent event) {
        Member receiver = memberReadService.getMember(event.receiverId());
        Notification notification = createNotification(receiver, event.url(),
            event.content());
        notificationRepository.save(notification);
        log.info("[ChannelTopic] :: {}", channelTopic.getTopic());
        log.info("[notification.toAlertMessage()] :: {}", notification.toAlertMessage().toString());
        redisTemplate.convertAndSend(channelTopic.getTopic(), notification.toAlertMessage());
    }

    private Notification createNotification(Member receiver, String url, String content) {
        return Notification.builder()
            .receiver(receiver)
            .url(url)
            .content(content)
            .isRead(false)
            .build();
    }
}
