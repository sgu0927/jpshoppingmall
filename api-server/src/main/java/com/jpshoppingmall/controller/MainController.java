package com.jpshoppingmall.controller;

import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProfileImageReadService profileImageReadService;
    private final CategoryReadService categoryReadService;
    private final ProductReadService productReadService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUser, Model model) {
        model.addAttribute("tonNineProducts", productReadService.getTopNineProductWithImages());
        model.addAttribute("categoryList", categoryReadService.getCategories());

        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
        }
        return "main";
    }
}