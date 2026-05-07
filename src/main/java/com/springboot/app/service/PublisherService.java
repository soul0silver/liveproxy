package com.springboot.app.service;

import com.springboot.app.dto.PublisherRequest;
import com.springboot.app.entity.Publisher;
import org.springframework.data.domain.Page;

public interface PublisherService {
    Page<Publisher> list(int page, int size);

    Publisher getById(Long id);

    Publisher create(PublisherRequest request);

    Publisher update(Long id, PublisherRequest request);

    void delete(Long id);
}
