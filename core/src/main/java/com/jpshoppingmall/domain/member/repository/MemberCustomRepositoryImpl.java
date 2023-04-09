package com.jpshoppingmall.domain.member.repository;

import static com.jpshoppingmall.domain.member.entity.QMember.member;

import com.jpshoppingmall.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory query;

    public Member getMemberWithProfileImage(Long memberId) {
        return query
            .selectFrom(member)
            .join(member.profileImage)
            .where(member.id.eq(memberId))
            .fetchOne();
    }
}
