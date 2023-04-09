package com.jpshoppingmall.domain.product.service;

import com.jpshoppingmall.domain.product.entity.ProductImage;
import com.jpshoppingmall.domain.product.repository.ProductImageRepository;
import com.jpshoppingmall.util.FileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductImageReadService {

    private final FileStore fileStore;
    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public List<String> getStoredImagePathList(Long productId) {
        final List<ProductImage> productImageList = productImageRepository.findByProductId(
            productId);
        return productImageList.stream().map(ProductImage::getStoreFileName)
            .map(fileStore::getFullPath).toList();
    }
}
