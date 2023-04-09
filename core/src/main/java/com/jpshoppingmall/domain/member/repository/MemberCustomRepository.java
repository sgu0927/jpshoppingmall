package com.jpshoppingmall.domain.member.repository;

import com.jpshoppingmall.domain.member.entity.Member;

public interface MemberCustomRepository {

    Member getMemberWithProfileImage(Long memberId);
}
