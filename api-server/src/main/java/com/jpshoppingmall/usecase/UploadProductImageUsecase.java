package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.product.service.ProductImageWriteService;
import com.jpshoppingmall.domain.product.service.ProductReadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UploadProductImageUsecase {

    private final ProductReadService productReadService;
    private final ProductImageWriteService productImageWriteService;

    @Transactional
    public void execute(Long productId, MultipartFile image, Boolean isUpdate, Boolean isTitle)
        throws IOException {
        var product = productReadService.getProduct(productId);
        Long savedImageId;
        if (isUpdate) {
            savedImageId = productImageWriteService.updateProductTitleImage(product, image);
        } else {
            savedImageId = productImageWriteService.upload(product, image, isTitle);
        }
    }
}
