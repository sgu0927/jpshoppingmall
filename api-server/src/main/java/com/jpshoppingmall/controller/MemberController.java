package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.service.AuthService;
import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.auth.vo.AuthBaseVo;
import com.jpshoppingmall.auth.vo.AuthenticationVo;
import com.jpshoppingmall.common.NotificationImage;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.member.service.MemberWriteService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.member.vo.ChangeEmailForm;
import com.jpshoppingmall.domain.member.vo.ChangeNicknameForm;
import com.jpshoppingmall.domain.member.vo.ChangePasswordForm;
import com.jpshoppingmall.domain.member.vo.EmailObject;
import com.jpshoppingmall.domain.notification.service.NotificationReadService;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.service.EmailVerificationProducerService;
import com.jpshoppingmall.type.EnumMaster.GenderType;
import com.jpshoppingmall.type.EnumMaster.Role;
import com.jpshoppingmall.usecase.UploadMemberImageUsecase;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final AuthService authService;
    private final CategoryReadService categoryReadService;
    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;
    private final EmailVerificationProducerService emailVerificationProducerService;
    private final ProfileImageReadService profileImageReadService;
    private final UploadMemberImageUsecase uploadMemberImageUsecase;
    private final NotificationReadService notificationReadService;

    @GetMapping("/login")
    public String loginForm(Model model, HttpServletRequest request,
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "exception", required = false) String exception) {
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("loginForm", new AuthBaseVo());
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "member/login";
    }

    @GetMapping(value = "/join")
    public String joinForm(Model model) {
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("genderTypes", GenderType.values());
        model.addAttribute("memberForm", new AuthenticationVo());
        return "member/join";
    }

    @PostMapping(value = "/join")
    public String join(@Valid @ModelAttribute AuthenticationVo memberForm,
        RedirectAttributes redirectAttributes) {
        memberForm.setRole(Role.USER.name());
        authService.signUp(memberForm);
        redirectAttributes.addFlashAttribute("registerComplete", "회원가입이 완료되었습니다.");
        return "redirect:/login";
    }

    @GetMapping(value = "/members/{memberId}")
    public String myPage(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        @RequestParam String currentMenu,
        Model model
    ) {
        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        if(notificationReadService.getHasUnreadNotification(customUser.getMemberId())){
            model.addAttribute("notificationImagePath", NotificationImage.HAS_UNREAD);
        } else {
            model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
        }

        model.addAttribute("profileImagePath",
            profileImageReadService.getMemberProfileImagePath(memberId));
        model.addAttribute("member", memberReadService.getMemberDto(memberId));
        model.addAttribute("memberId", memberId);
        model.addAttribute("currentMenu", currentMenu);

        return "member/my-page";
    }

    @PostMapping(value = "/members/{memberId}/nickname")
    public String changeNickname(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        @Valid @RequestBody ChangeNicknameForm nicknameForm,
        RedirectAttributes redirectAttributes
    ) {
        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        memberWriteService.changeNickname(memberId, nicknameForm.getToNickname());

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/{memberId}?currentMenu=profile";
    }

    @PostMapping(value = "/members/{memberId}/password")
    public String changePassword(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        @Valid @RequestBody ChangePasswordForm passwordForm,
        RedirectAttributes redirectAttributes
    ) {
        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        authService.changePassword(memberId, passwordForm.getFromPassword(),
            passwordForm.getToPassword());

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/{memberId}?currentMenu=profile";
    }

    @PostMapping(value = "/members/{memberId}/email")
    public String changeEmail(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        @Valid @RequestBody ChangeEmailForm emailForm,
        RedirectAttributes redirectAttributes
    ) {
        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        memberWriteService.changeEmail(memberId, emailForm.getToEmail());

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/{memberId}?currentMenu=profile";
    }

    @ResponseBody
    @PostMapping(value = "/email/verification")
    public void sendVerificationMail(
        @RequestBody EmailObject emailObject
    ) {
        log.info("email :: {}", emailObject.email());
        emailVerificationProducerService.sendMessage(emailObject.email());
    }

    @GetMapping("/email/verification")
    public ResponseEntity<?> checkVerificationCode(@RequestParam String code,
        @RequestParam String email) {
        return ResponseEntity.ok(authService.checkEmailVerificationCode(code, email));
    }

    @Operation(summary = "회원 닉네임 중복 확인")
    @ResponseBody
    @GetMapping("/members/duplicateCheck/nickname")
    public ResponseEntity<?> nickNameDuplicateCheck(@RequestParam String nickname) {

        return ResponseEntity.ok(memberReadService.nicknameDuplicateCheck(nickname));
    }

    @Operation(summary = "회원 이메일 중복 확인")
    @ResponseBody
    @GetMapping("/members/duplicateCheck/email")
    public ResponseEntity<?> emailDuplicateCheck(@RequestParam String email) {

        return ResponseEntity.ok(memberReadService.emailDuplicateCheck(email));
    }

    @Operation(summary = "회원 프로필 사진 업로드")
    @PostMapping(value = "/members/{memberId}/profile-image", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadProfileImage(
        @PathVariable Long memberId,
        @RequestPart(name = "image") MultipartFile image
    ) throws IOException {
        uploadMemberImageUsecase.execute(memberId, image, false);
    }

    @Operation(summary = "회원 프로필 사진 변경")
    @PutMapping(value = "/members/{memberId}/profile-image", consumes = {
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public void changeProfileImage(
        @PathVariable Long memberId,
        @RequestPart(name = "image") MultipartFile image
    ) throws IOException {
        uploadMemberImageUsecase.execute(memberId, image, true);
    }
}
