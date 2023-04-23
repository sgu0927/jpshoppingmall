package com.jpshoppingmall.domain.notification.repository;

import com.jpshoppingmall.domain.notification.entity.Notification;
import java.util.List;

public interface NotificationCustomRepository {
    Boolean existsReadOrNotMemberNotification(Boolean isRead, Long memberId);

    List<Notification> getMemberNotification(Boolean isRead, Long memberId);
}
