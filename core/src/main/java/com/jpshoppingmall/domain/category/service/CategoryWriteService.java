package com.jpshoppingmall.domain.category.service;

import com.jpshoppingmall.domain.category.dto.CategoryDto;
import com.jpshoppingmall.domain.category.dto.RegisterCategoryDto;
import com.jpshoppingmall.domain.category.dto.UpdateCategoryDto;
import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.category.repository.CategoryRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryWriteService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category addCategory(RegisterCategoryDto categoryDto) {
        Category category = Category.builder()
            .categoryName(categoryDto.categoryName())
            .build();

        return categoryRepository.save(category);
    }

    @Transactional
    public CategoryDto updateCategory(UpdateCategoryDto categoryDto, Category category,
        Category parent, boolean hasParent) {

        if (categoryDto.parentCategoryId() == null) {
            if (hasParent) {
                parent.getChild().remove(category);
            }
            var savedCategory = categoryRepository.save(Category.builder()
                .id(category.getId())
                .categoryName(categoryDto.categoryName())
                .parent(null)
                .build());

            return new CategoryDto(savedCategory.getId(), savedCategory.getCategoryName(), null,false);
        } else {
            Category newParent = categoryRepository.findById(categoryDto.parentCategoryId())
                .orElseThrow(() -> new CommonException(CommonExceptionType.CANNOT_FOUND_CATEGORY));
            if (hasParent) {
                parent.getChild().remove(category);
            }
            newParent.addChildCategory(category);

            var savedCategory = categoryRepository.save(Category.builder()
                .id(category.getId())
                .categoryName(categoryDto.categoryName())
                .parent(newParent)
                .build());

            return savedCategory.toDto(newParent.getId(), false);
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
