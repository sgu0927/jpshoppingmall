var changeInfoValidCb = {
    // 닉네임, 이메일 중복 체크 후 변경 될 가능성이 존재하니 임시로 저장하는 값
    tempNickname		: "",
    tempEmail		: "",
    emailDupChk	: false,
    emailVerificationChk	: false,

    duplicateNicknameChkFnCb : function() {
        var that = this;

        var nickname = $("#change-nickname").val();

        $.ajax({
            url : "/members/duplicateCheck/nickname?nickname=" + nickname,
            type: "get",
            contentType : "application/json",
            async : false,
            success : function (data) {
                if (data) {
                    alert("사용 가능한 닉네임 입니다!");
                    that.tempNickname   = nickname;
                }
            },
            error : function (e) {
                alert(e.responseText);
            }
        })
    },

    // 이메일 중복 체크
    duplicateEmailChkFnCb : function() {
        var that = this;
        var email = $("#change-email").val();

        $.ajax({
            url : "/members/duplicateCheck/email?email=" + email,
            type: "get",
            contentType : "application/json",
            async : false,
            success : function (data) {
                if (data) {
                    alert("사용 가능한 이메일 입니다!");
                    that.emailDupChk = true;
                    that.tempEmail   = email;
                }
            },
            error : function (e) {
                alert(e.responseText);
            }
        })
    },

    verificationCodeChkFnCb : function() {
        var that = this;

        var email = $("input[name='email']").val();
        var code = $("input[name='verificationCode']").val();

        $.ajax({
            type : "GET",
            url  : "/email/verification?email=" + email + "&code=" + code,
            contentType : "application/json",
            async : false,
            success : function(res) {
                if(res) {
                    alert("이메일 인증이 완료되었습니다.");
                    that.emailVerificationChk = true;
                }
            },
            error   : function(e) {
                that.emailVerificationChk = false;
                alert(e.responseText);
            }
        })
    },

    changeNicknameValidChkFnCb : function() {
        var nickname = $("#change-nickname").val();

        if (this.tempNickname !== nickname) {
            alert("닉네임 중복 체크 해주세요!")
            $("#nicknameDupChkBtn").focus();
            return false;
        }

        return true;
    },

    changePasswordValidChkFnCb : function() {
        var password = $("#change-password").val();
        var passwordConfirm = $("#change-password-confirm").val();

        if (password !== passwordConfirm) {
            alert("비밀번호가 일치하지 않습니다(비밀번호 확인)")
            $("#change-password-confirm").focus();
            return false;
        }

        return true;
    },

    changeEmailValidChkFnCb : function() {
        var email = $("#change-email").val();

        if (this.tempEmail !== email) {
            alert("이메일 중복 체크 해주세요!")
            $("#emailDupChkBtn").focus();
            return false;
        }

        if (!this.emailVerificationChk) {
            alert("이메일 인증을 완료해주세요!");
            $("#emailVerifyBtn").focus();
            return false;
        }

        return true;
    }
}