package com.jpshoppingmall.auth.vo;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationVo extends AuthBaseVo{
    @NotBlank
    private String nickname;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String role;

    private String genderType;
    private String birthday;
}
