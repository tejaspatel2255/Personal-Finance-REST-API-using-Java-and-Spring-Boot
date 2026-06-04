package com.financeapi.service;

import com.financeapi.dto.CategoryRequest;
import com.financeapi.dto.CategoryResponse;
import com.financeapi.exception.ResourceNotFoundException;
import com.financeapi.exception.UnauthorizedActionException;
import com.financeapi.model.Category;
import com.financeapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserContextService userContextService;

    public CategoryResponse create(CategoryRequest request) {
        var user = userContextService.currentUser();
        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(user)
                .build();
        category = categoryRepository.save(category);
        return toResponse(category);
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findByUserId(userContextService.currentUser().getId())
                .stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        var userId = userContextService.currentUser().getId();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("You do not own this category");
        }
        categoryRepository.delete(category);
    }

    public Category requireOwnedCategory(Long categoryId) {
        return categoryRepository.findByIdAndUserId(categoryId, userContextService.currentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder().id(category.getId()).name(category.getName()).type(category.getType()).build();
    }
}
