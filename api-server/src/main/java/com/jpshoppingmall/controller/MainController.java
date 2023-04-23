package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.NotificationImage;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.MemberReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.notification.dto.NotificationCreateEvent;
import com.jpshoppingmall.domain.notification.service.NotificationReadService;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import com.jpshoppingmall.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProfileImageReadService profileImageReadService;
    private final CategoryReadService categoryReadService;
    private final ProductReadService productReadService;
    // for test
    private final NotificationService notificationService;
    private final NotificationReadService notificationReadService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUser, Model model) {
        model.addAttribute("tonNineProducts", productReadService.getTopNineProductWithImages());
        model.addAttribute("categoryList", categoryReadService.getCategories());

        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
            model.addAttribute("memberId", customUser.getMemberId());
            if(notificationReadService.getHasUnreadNotification(customUser.getMemberId())){
                model.addAttribute("notificationImagePath", NotificationImage.HAS_UNREAD);
            } else {
                model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
            }
        }
        return "main";
    }

    @ResponseBody
    @PostMapping("/test/{memberId}")
    public void testAlert(@PathVariable Long memberId) {
        notificationService.sendCreateEvent(
            new NotificationCreateEvent("test url", "testContents",memberId));
    }
}