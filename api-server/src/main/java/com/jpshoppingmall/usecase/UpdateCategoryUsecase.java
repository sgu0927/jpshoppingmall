package com.jpshoppingmall.usecase;

import com.jpshoppingmall.domain.category.dto.CategoryDto;
import com.jpshoppingmall.domain.category.dto.UpdateCategoryDto;
import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.category.service.CategoryReadService;
import com.jpshoppingmall.domain.category.service.CategoryWriteService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateCategoryUsecase {

    private final CategoryReadService categoryReadService;
    private final CategoryWriteService categoryWriteService;

    @Transactional
    public CategoryDto execute(Long id, UpdateCategoryDto updateCategoryDto) {
        Category category = categoryReadService.getCategoryById(id);
        Optional<Category> parent = Optional.ofNullable(category.getParent());
        if (parent.isPresent()) {
            return categoryWriteService.updateCategory(updateCategoryDto, category, parent.get(),
                true);
        }
        return categoryWriteService.updateCategory(updateCategoryDto, category, null, false);
    }
}
