package com.springboot.app.service.impl;

import com.springboot.app.dto.PublisherRequest;
import com.springboot.app.entity.Publisher;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.PublisherRepo;
import com.springboot.app.service.PublisherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepo publisherRepo;

    @Override
    public Page<Publisher> list(int page, int size) {
        return publisherRepo.findAll(PageRequest.of(page, size));
    }

    @Override
    public Publisher getById(Long id) {
        return publisherRepo.findById(id)
                .orElseThrow(() -> new BusinessEx("Publisher not found"));
    }

    @Override
    public Publisher create(PublisherRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.getName());
        publisher.setAddress(request.getAddress());
        publisher.setPhone(request.getPhone());
        publisher.setEmail(request.getEmail());
        return publisherRepo.save(publisher);
    }

    @Override
    public Publisher update(Long id, PublisherRequest request) {
        Publisher publisher = getById(id);
        publisher.setName(request.getName());
        publisher.setAddress(request.getAddress());
        publisher.setPhone(request.getPhone());
        publisher.setEmail(request.getEmail());
        return publisherRepo.save(publisher);
    }

    @Override
    public void delete(Long id) {
        Publisher publisher = getById(id);
        publisherRepo.delete(publisher);
    }
}
