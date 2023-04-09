package com.jpshoppingmall.auth.service;

import com.jpshoppingmall.auth.vo.AuthenticationVo;
import com.jpshoppingmall.common.Address;
import com.jpshoppingmall.domain.member.entity.Member;
import com.jpshoppingmall.domain.member.repository.MemberRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.type.EnumMaster.GenderType;
import com.jpshoppingmall.type.EnumMaster.Role;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private static final String TEST_ACCOUNT_EMAIL = "test@test.com";
    private static final String TEST_ACCOUNT_PASSWORD = "test@test.com";

    @PostConstruct
    public void init() {
        if (!memberRepository.existsByEmail(TEST_ACCOUNT_EMAIL)) {
            memberRepository.save(Member
                .builder()
                .role(Role.USER)
                .email(TEST_ACCOUNT_EMAIL)
                .password(passwordEncoder.encode(TEST_ACCOUNT_PASSWORD))
                .name("TESTER")
                .nickname("테스터")
                .genderType(GenderType.M)
                .phone("01012345678")
                .address(new Address("42749", "대구시 달서구 월곡로 320", "110동 110호"))
                .birthday(LocalDate.of(2023, 3, 22))
                .build());
            log.info("Test account saved");
        }
    }

    @Transactional
    public void signUp(AuthenticationVo authenticationVo) {
        if (authenticationVo == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS,
                ", authenticationVo is null");
        }

        authenticationVo.checkParams();

        if (memberRepository.existsByEmail(authenticationVo.getEmail())) {
            throw new CommonException(CommonExceptionType.DUPLICATE_MEMBER_EMAIL,
                ", email is already exist");
        }

        final Member member = memberRepository.save(Member
            .builder()
            .role(Role.valueOf(authenticationVo.getRole()))
            .email(authenticationVo.getEmail())
            .password(passwordEncoder.encode(authenticationVo.getPassword()))
            .name(authenticationVo.getName())
            .nickname(authenticationVo.getNickname())
            .birthday(LocalDate.parse(authenticationVo.getBirthday(),
                DateTimeFormatter.ofPattern("yyyyMMdd")))
            .genderType(GenderType.valueOf(authenticationVo.getGenderType()))
            .build());
    }

    @Transactional
    public void changePassword(Long memberId, String fromPassword, String toPassword) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(fromPassword, member.getPassword())) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS,
                ", password is incorrect");
        }

        validatePassword(toPassword);
        member.changePassword(passwordEncoder.encode(toPassword));
    }

    public boolean checkEmailVerificationCode(String code, String memberEmail) {
        String email = redisTemplate.opsForValue().get(code);
        if (email == null) {
            throw new CommonException(CommonExceptionType.EXPIRED_EMAIL_VERIFICATION_CODE);
        }

        if (email.equals(memberEmail)) {
            return true;
        } else {
            throw new CommonException(CommonExceptionType.INVALID_EMAIL_VERIFICATION_CODE);
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 6) {
            throw new CommonException(CommonExceptionType.TOO_SHORT_PASSWORD);
        }
        if (password.length() > 20) {
            throw new CommonException(CommonExceptionType.TOO_LONG_PASSWORD);
        }
    }
}
