package com.jpshoppingmall.domain.member.service;

import com.jpshoppingmall.domain.member.dto.MemberDto;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.repository.MemberRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_MEMBER));
    }

    public MemberDto getMemberDto(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_MEMBER))
            .toDto();
    }

    public boolean nicknameDuplicateCheck(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new CommonException(CommonExceptionType.DUPLICATE_MEMBER_NICKNAME);
        }
        return true;
    }

    public boolean emailDuplicateCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CommonException(CommonExceptionType.DUPLICATE_MEMBER_EMAIL);
        }
        return true;
    }
}
