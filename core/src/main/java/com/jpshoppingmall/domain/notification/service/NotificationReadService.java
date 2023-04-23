package com.jpshoppingmall.domain.notification.service;

import com.jpshoppingmall.domain.notification.entity.Notification;
import com.jpshoppingmall.domain.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationReadService {

    private final NotificationRepository notificationRepository;

    public boolean getHasUnreadNotification(Long memberId) {
        return notificationRepository.existsReadOrNotMemberNotification(false, memberId);
    }

    public List<Notification> getMemberNotifications(boolean isRead, Long memberId) {
        return notificationRepository.getMemberNotification(isRead, memberId);
    }
}
