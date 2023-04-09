package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.service.ProductImageWriteService;
import com.jpshoppingmall.domain.product.service.ProductWriteService;
import com.jpshoppingmall.domain.product.service.PurchaseCountWriteService;
import com.jpshoppingmall.domain.product.vo.ProductRegisterForm;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AddProductWithImagesUsecase {

    private final CategoryReadService categoryReadService;
    private final ProductWriteService productWriteService;
    private final ProductImageWriteService productImageWriteService;
    private final PurchaseCountWriteService purchaseCountWriteService;

    @Transactional
    public Long execute(ProductRegisterForm productForm) throws IOException {
        Category category = categoryReadService.getCategoryById(productForm.getCategoryId());
        Product product = productWriteService.addProduct(category, productForm);

        MultipartFile titleImage = productForm.getTitleImageFile();
        List<MultipartFile> detailImages = productForm.getDetailImageFiles();
        if (titleImage != null) {
            productImageWriteService.upload(product, titleImage, true);
        }
        if (!detailImages.isEmpty()) {
            for (MultipartFile detailImage : detailImages) {
                productImageWriteService.upload(product, detailImage, false);
            }
        }

        // productCount 생성
        purchaseCountWriteService.saveNewProductCount(product.getId());

        return product.getId();
    }
}
