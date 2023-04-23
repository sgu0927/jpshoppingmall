package com.jpshoppingmall.domain.notification.dto;

public record NotificationCreateEvent(
    String url,
    String content,
    Long receiverId
) {

}
