package com.jpshoppingmall.controller;

import com.jpshoppingmall.auth.userdetails.CustomUserDetails;
import com.jpshoppingmall.common.NotificationImage;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.member.service.ProfileImageReadService;
import com.jpshoppingmall.domain.notification.service.NotificationReadService;
import com.jpshoppingmall.domain.product.dto.ProductDetailDto;
import com.jpshoppingmall.domain.product.vo.AddCartForm;
import com.jpshoppingmall.domain.product.vo.ProductRegisterForm;
import com.jpshoppingmall.service.PurchaseCountService;
import com.jpshoppingmall.type.EnumMaster.PagingType;
import com.jpshoppingmall.type.EnumMaster.SortType;
import com.jpshoppingmall.usecase.AddProductWithImagesUsecase;
import com.jpshoppingmall.usecase.GetCategoryProductUsecase;
import com.jpshoppingmall.usecase.GetProductDetailUsecase;
import com.jpshoppingmall.util.PageRequestUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final AddProductWithImagesUsecase addProductWithImagesUsecase;
    private final CategoryReadService categoryReadService;
    private final ProfileImageReadService profileImageReadService;
    private final PurchaseCountService purchaseCountService;
    private final GetCategoryProductUsecase getCategoryProductUsecase;
    private final GetProductDetailUsecase getProductDetailUsecase;
    private final NotificationReadService notificationReadService;

    @GetMapping("/category-product/{categoryId}")
    public String categoryProductPage(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long categoryId,
        PageRequestUtil pageRequest,
        Model model
    ) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
            if(notificationReadService.getHasUnreadNotification(customUser.getMemberId())){
                model.addAttribute("notificationImagePath", NotificationImage.HAS_UNREAD);
            } else {
                model.addAttribute("notificationImagePath", NotificationImage.ALL_READ);
            }
        }

        Page<ProductDetailDto> productDetailDtoPage = getCategoryProductUsecase.execute(categoryId,
            pageRequest.of(
                PagingType.PRODUCT));

        model.addAttribute("products", productDetailDtoPage);
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortType", getSortType(pageRequest.of(PagingType.PRODUCT).getSort().toString()));
        model.addAttribute("pageNum", pageRequest.of(PagingType.PRODUCT).getPageNumber());

        return "product/category-product";
    }

    @GetMapping(value = "/product-register")
    public String productRegisterForm(@AuthenticationPrincipal CustomUserDetails customUser,
        Model model) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
        } else {
            model.addAttribute("profileImagePath",
                profileImageReadService.getMemberProfileImagePath(customUser.getMemberId()));
        }

        model.addAttribute("productForm", new ProductRegisterForm());
        model.addAttribute("categoryList", categoryReadService.getCategories());
        return "product/product-register";
    }

    @PostMapping("/product-register")
    public String productRegister(@ModelAttribute ProductRegisterForm productForm,
        RedirectAttributes redirectAttributes) throws IOException {
        Long savedProductId = addProductWithImagesUsecase.execute(productForm);

        redirectAttributes.addAttribute("productId", savedProductId);

        return "redirect:/products/{productId}";
    }

    @GetMapping("/products/{productId}")
    public String productDetail(
        @AuthenticationPrincipal CustomUserDetails customUser,
        @PathVariable Long productId,
        Model model
    ) {
        if (customUser == null) {
            model.addAttribute("profileImagePath", null);
            model.addAttribute("memberId", null);
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

        model.addAttribute("addCartForm", new AddCartForm());
        model.addAttribute("categoryList", categoryReadService.getCategories());
        model.addAttribute("productDetail", getProductDetailUsecase.execute(productId));
        model.addAttribute("purchaseCount",
            purchaseCountService.getCurrentPurchaseCount(productId));

        return "product/product-detail";
    }

    private String getSortType(String sortType) {
        return switch (sortType) {
            case "price: DESC" -> "PRICE_DESC";
            case "price: ASC" -> "PRICE_ASC";
            case "createdDateTime: DESC" -> "NEWEST";
            case "rating: DESC" -> "RATING_DESC";
            case "discountPercent: DESC" -> "DISCOUNT_PERCENT_DESC";
            default -> "RATING_DESC";
        };
    }
}
