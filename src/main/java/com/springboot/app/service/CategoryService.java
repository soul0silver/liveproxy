package com.springboot.app.service;

import com.springboot.app.dto.CategoryRequest;
import com.springboot.app.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<Category> list(int page, int size, String name);

    Category getById(Long id);

    Category create(CategoryRequest request);

    Category update(Long id, CategoryRequest request);

    void delete(Long id);
}
