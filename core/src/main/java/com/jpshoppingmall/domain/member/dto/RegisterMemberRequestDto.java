package com.jpshoppingmall.domain.member.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record RegisterMemberRequestDto(
    String role,
    @NotBlank
    @Email
    String email,
    @NotBlank
    String name,
    @NotBlank
    String password,
    String phone,
    LocalDate birthday,
    String genderType,
    @NotBlank
    String nickname
) {
}
