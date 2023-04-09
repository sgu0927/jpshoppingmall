package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.entity.Product;
import com.jpshoppingmall.domain.product.entity.ProductImage;
import com.jpshoppingmall.domain.product.repository.ProductImageRepository;
import com.jpshoppingmall.util.FileStore;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductImageWriteService {

    private final FileStore fileStore;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public Long upload(Product product, MultipartFile file, Boolean isTitle)
        throws IOException {
        ProductImage productImage = fileStore.storeFile(product, file, isTitle);
        var savedProductImage = productImageRepository.save(productImage);

        return savedProductImage.getId();
    }

    @Transactional
    public Long updateProductTitleImage(Product product, MultipartFile file)
        throws IOException {
        List<ProductImage> productImages = productImageRepository.findByProductId(
            product.getId());

        Optional<ProductImage> optionalTitleImage = productImages.stream()
            .filter(ProductImage::getIsTitle).findAny();
        if (optionalTitleImage.isPresent()) {
            ProductImage productImage = fileStore.updateFile(optionalTitleImage.get(), file, true);
            var savedProductImage = productImageRepository.save(productImage);
            return savedProductImage.getId();
        } else {
            log.info("Product Title Image doesn't exists! - memberId :: {}", product.getId());
            return null;
        }

    }
}
