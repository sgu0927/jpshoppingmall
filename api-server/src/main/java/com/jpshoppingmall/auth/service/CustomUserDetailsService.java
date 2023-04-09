package com.jpshoppingmall.auth.service;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return new CustomUserDetails(memberRepository.findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")
            )
        );
    }
}
