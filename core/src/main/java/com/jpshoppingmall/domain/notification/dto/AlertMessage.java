package com.jpshoppingmall.domain.notification.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlertMessage implements Serializable {
    private Long memberId;
    private String content;
    private String url;
}
