package com.jpshoppingmall.domain.member.entity;

import com.jpshoppingmall.common.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
public class ProfileImage extends BaseTimeEntity {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "profileImage")
    private Member member;
    // 고객이 업로드한 파일명
    private String originalFileName;
    // 서버 내부에서 관리하는 파일명
    private String storeFileName;

}
