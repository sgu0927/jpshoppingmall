package com.jpshoppingmall.domain.product.vo;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRegisterForm {
    private Long categoryId;
    private String name;
    private Integer price;
    private Integer totalCount;
    private Integer discountPercent;
    private MultipartFile titleImageFile;
    private List<MultipartFile> detailImageFiles;
}
