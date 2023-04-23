package com.jpshoppingmall.domain.notification.repository;

import static com.jpshoppingmall.domain.member.entity.QMember.member;
import static com.jpshoppingmall.domain.notification.entity.QNotification.notification;

import com.jpshoppingmall.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Boolean existsReadOrNotMemberNotification(Boolean isRead, Long memberId) {
        Integer fetchFirst = query
            .selectOne()
            .from(notification)
            .where(notification.receiver.id.eq(memberId), notification.isRead.eq(isRead))
            .fetchFirst();

        return fetchFirst != null;
    }

    public List<Notification> getMemberNotification(Boolean isRead, Long memberId) {
        return query
            .selectFrom(notification)
            .where(notification.receiver.id.eq(memberId), notification.isRead.eq(isRead))
            .fetch();
    }
}
