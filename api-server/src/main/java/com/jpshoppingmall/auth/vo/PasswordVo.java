package com.jpshoppingmall.auth.vo;

import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordVo {
    protected String oldPassword;
    protected String newPassword;

    public void checkParams() {
        if (this.oldPassword == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, ", oldPassword is null");
        }
        if (this.newPassword == null) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, ", newPassword is null");
        }
    }
}
