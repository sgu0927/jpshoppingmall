package com.jpshoppingmall.domain.notification.entity;

import com.jpshoppingmall.common.BaseTimeEntity;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.notification.dto.AlertMessage;
import com.jpshoppingmall.util.BooleanToYNConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends BaseTimeEntity {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String content;
    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, length = 1)
    private Boolean isRead;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member receiver;

    public AlertMessage toAlertMessage(){
        return AlertMessage.builder()
            .memberId(receiver.getId())
            .content(content)
            .url(url)
            .build();
    }

    public void read() {
        this.isRead = true;
    }
}
