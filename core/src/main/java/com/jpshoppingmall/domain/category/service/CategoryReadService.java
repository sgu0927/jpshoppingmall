package com.jpshoppingmall.domain.category.service;

import com.jpshoppingmall.domain.category.dto.CategoryDto;
import com.jpshoppingmall.domain.category.entity.Category;
import com.jpshoppingmall.domain.category.repository.CategoryRepository;
import com.jpshoppingmall.exception.CommonException;
import com.jpshoppingmall.exception.CommonExceptionType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryReadService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
            .map(category -> new CategoryDto(category.getId(), category.getCategoryName(),
                category.getParent() == null ? null : category.getParent().getId(),
                !category.getChild().isEmpty())).toList();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CommonException(
            CommonExceptionType.CANNOT_FOUND_CATEGORY));
    }

    @Transactional(readOnly = true)
    public List<Long> getChildCategoryIds(Long fromCategoryId) {
        List<Long> categoryIds = new ArrayList<>();

        Queue<Category> queue = new LinkedList<>();
        queue.add(getCategoryById(fromCategoryId));
        while (!queue.isEmpty()) {
            Category currentCategory = queue.poll();
            categoryIds.add(currentCategory.getId());

            List<Category> childNodes = currentCategory.getChild();
            if (!childNodes.isEmpty()) {
                queue.addAll(childNodes);
            }
        }

        return categoryIds;
    }
}
