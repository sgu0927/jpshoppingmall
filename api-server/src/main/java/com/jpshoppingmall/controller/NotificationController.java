package com.jpshoppingmall.controller;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.NotificationImage;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.notification.service.NotificationReadService;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import com.jpshoppingmall.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final ProfileImageReadService profileImageReadService;
    private final NotificationService notificationService;
    private final NotificationReadService notificationReadService;

    @GetMapping(value = "/members/{memberId}/notifications")
    public String myNotification(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        @RequestParam String currentMenu,
        Model model
    ) {
        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        model.addAttribute("profileImagePath",
            profileImageReadService.getMemberProfileImagePath(memberId));
        model.addAttribute("memberId", memberId);
        model.addAttribute("currentMenu", currentMenu);

        if(Objects.equals(currentMenu, "unread")) {
            model.addAttribute("notifications",
                notificationReadService.getMemberNotifications(false, memberId));
        } else {
            model.addAttribute("notifications",
                notificationReadService.getMemberNotifications(true, memberId));
        }

        if(notificationReadService.getHasUnreadNotification(customUser.getMemberId())){
            model.addAttribute("notificationImagePath", NotificationImage.HAS_UNREAD);
        } else {
            model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
        }

        return "notification/my-notification";
    }

    //    @PreAuthorize("hasAuthority('alarm.read')")
    @Operation(summary = "알람 sse 구독", description = "알람 sse 구독, sseEmitter객체 반환.")
    @ResponseBody
    @GetMapping(value = "/members/{memberId}/alarm/subscribe", produces = TEXT_EVENT_STREAM_VALUE)
    public SseEmitter alarmSubscribe(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long memberId,
        HttpServletResponse response) {

        if (customUser == null || !Objects.equals(customUser.getMemberId(), memberId)) {
            throw new CommonException(CommonExceptionType.INVALID_PARAMS, "잘못된 memberId 접근입니다.");
        }

        //nginx리버스 프록시에서 버퍼링 기능으로 인한 오동작 방지
        response.setHeader("X-Accel-Buffering", "no");
        return notificationService.subscribe(customUser.getMemberId(), LocalDateTime.now());
    }
}
