// 유효성 콜백
var validCallback = {

    // 닉네임, 이메일 중복 체크 후 변경 될 가능성이 존재하니 임시로 저장하는 값
    tempNickname		: "",
    tempEmail		: "",
    nicknameDupChk	: false,
    emailDupChk	: false,
    emailVerificationChk	: false,

    // 닉네임 중복 체크
    duplicateNicknameChkFnCb : function() {
        var that = this;

        var nickname = $("#register-nickname").val();

        $.ajax({
            url : "/members/duplicateCheck/nickname?nickname=" + nickname,
            type: "get",
            contentType : "application/json",
            async : false,
            success : function (data) {
                if (data) {
                    alert("사용 가능한 닉네임 입니다!");
                    that.nicknameDupChk = true;
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

        var email = $("#register-email").val();

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

    // 빈값 체크
    emptyChkFnCb : function() {
        var valid = false;

        $("input.registrationInput").each(function() {
            var $this = $(this),
                removeBlankData = $this.val().replace(" ", ""),
                // removeBlankData = $this.val().replace(Regex.blank, ""),     // 빈칸 (스페이스바) 입력 시 공백지움
                dataName = $this.data("name");

            $this.val(removeBlankData);

            // 공백이 아닐 경우 if
            if ($this.val() !== "") {
                valid = true;
            } else {
                var text = dataName;

                alert(text + "은/는 필수 입력 값입니다.");

                $this.focus();
                valid = false;

                // each문 종료
                return false;
            }
        })
        return valid;
    },

    // 이메일 유효성 체크
//    emailValidChkFnCb : function(email) {
//        if (!Regex.email.test(email)) {
//            alert("유효하지 형식의 이메일 입니다!");
//
//            $("#register-email").focus();
//
//            return false;
//        }
//
//        return true;
//    },

//    pwdValidChkFnCb   : function(password) {
//        if (!Regex.password.test(password)) {
//            alert("유효하지 형식의 비밀번호 입니다!");
//
//            $("#register-password").focus();
//
//            return false;
//        }
//
//        return true;
//    },

    pwdConfirmValidChkFnCb    : function(password, pwdConfirm) {
        if (password !== pwdConfirm) {
            alert("첫번째 입력한 비밀번호와 같지 않습니다!");

            $("#register-passwordConfirm").focus();

            return false;
        }

        return true;
    },

    // 입력 데이터 유효성 체크
    registerValidChkFnCb : function() {
        var nickname = $("#register-nickname").val(),
            email = $("#register-email").val(),
            password = $("#register-password").val();

        if (this.tempNickname !== nickname) {

            alert("닉네임 중복 체크 해주세요!")

            $("#nicknameDupChkBtn").focus();

            return false;
        }

        if (this.tempEmail !== email) {

            alert("이메일 중복 체크 해주세요!")

            $("#emailDupChkBtn").focus();

            return false;
        }

        console.log(this.emailVerificationChk);

        if (!this.emailVerificationChk) {
            alert("이메일 인증을 완료해주세요!");

            $("#emailVerifyBtn").focus();

            return false;
        }

//        if (this.emptyChkFn() && this.emailValidChkFn($("#register-email").val())
//                    && this.pwdValidChkFn(password) && this.pwdConfirmValidChkFn(password, $("#register-passwordConfirm").val()))

        if (this.emptyChkFn()) {

            return true;
        } else
            return false;
    }
}