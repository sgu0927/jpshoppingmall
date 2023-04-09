package com.jpshoppingmall.auth.vo;

import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthBaseVo {
    @Email
    @NotBlank
    protected String email;
    @NotBlank
    protected String password;

    public void checkParams() {
        if (this.email == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, ", email is null");
        }
        if (this.password == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, ", password is null");
        }
    }
}
