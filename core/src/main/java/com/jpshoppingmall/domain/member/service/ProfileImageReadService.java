package com.jpshoppingmall.domain.member.service;

import com.jpshoppingmall.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImageReadService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public String getMemberProfileImagePath(Long memberId) {
        return memberRepository.getMemberWithProfileImage(memberId).getProfileImage()
            .getStoreFileName();
    }
}
