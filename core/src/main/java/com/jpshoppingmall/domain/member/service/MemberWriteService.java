package com.jpshoppingmall.domain.member.service;

import com.jpshoppingmall.domain.member.repository.MemberRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;

    @Transactional
    public void changeNickname(Long memberId, String nickname) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_MEMBER));
        member.changeNickname(nickname);
    }

    @Transactional
    public void changeEmail(Long memberId, String email) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_MEMBER));
        member.changeEmail(email);
    }
}
