package com.jpshoppingmall.domain.member.vo;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordForm {
    @NotBlank
    String fromPassword;
    @NotBlank
    String toPassword;
}
