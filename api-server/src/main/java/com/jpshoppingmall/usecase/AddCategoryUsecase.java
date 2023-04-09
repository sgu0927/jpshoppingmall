package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.category.dto.CategoryDto;
import com.jpshoppingmall.domain.category.dto.RegisterCategoryDto;
import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.category.service.CategoryWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddCategoryUsecase {

    private final CategoryReadService categoryReadService;
    private final CategoryWriteService categoryWriteService;

    @Transactional
    public CategoryDto addCategory(RegisterCategoryDto registerCategoryDto) {
        if (registerCategoryDto.parentCategoryId() == null) {
            return categoryWriteService.addCategory(registerCategoryDto).toDto(null, false);
        } else {
            Category parent = categoryReadService.getCategoryById(
                registerCategoryDto.parentCategoryId());
            Category child = categoryWriteService.addCategory(registerCategoryDto);
            parent.addChildCategory(child);
            return child.toDto(parent.getId(), false);
        }
    }
}
