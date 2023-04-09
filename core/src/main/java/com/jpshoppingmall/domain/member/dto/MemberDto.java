package com.jpshoppingmall.domain.member.dto;

import com.jpshoppingmall.common.Address;

public record MemberDto(
    String name,
    String email,
    String nickname,
    Address address,
    String phone
) {

}
