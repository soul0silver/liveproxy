package com.springboot.app.service.impl;

import com.springboot.app.dto.CategoryRequest;
import com.springboot.app.entity.Category;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.CategoryRepo;
import com.springboot.app.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    @Override
    public Page<Category> list(int page, int size, String name) {
        name = StringUtils.isNotBlank(name) ? "%" + name + "%" : "%%";
        return categoryRepo.findAllByNameLike(PageRequest.of(page, size), name);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new BusinessEx("Category not found"));
    }

    @Override
    public Category create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepo.save(category);
    }

    @Override
    public Category update(Long id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepo.save(category);
    }

    @Override
    public void delete(Long id) {
        Category category = getById(id);
        categoryRepo.delete(category);
    }
}
