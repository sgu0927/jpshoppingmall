package com.jpshoppingmall.domain.member.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailForm {
    @Email
    @NotBlank
    String toEmail;
}
