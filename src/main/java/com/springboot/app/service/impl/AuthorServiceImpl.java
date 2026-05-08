package com.springboot.app.service.impl;

import com.springboot.app.dto.AuthorRequest;
import com.springboot.app.entity.Author;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.AuthorRepo;
import com.springboot.app.service.AuthorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepo authorRepo;

    @Override
    public Page<Author> list(int page, int size, String name) {
        name = StringUtils.isNotBlank(name) ? "%" + name + "%" : "%%";
        return authorRepo.findAllByNameLikeAndDeletedIsFalse(PageRequest.of(page, size), name);
    }

    @Override
    public Author getById(Long id) {
        return authorRepo.findById(id)
                .orElseThrow(() -> new BusinessEx("Author not found"));
    }

    @Override
    public Author create(AuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setBirthDate(request.getBirthDate());
        return authorRepo.save(author);
    }

    @Override
    public Author update(Long id, AuthorRequest request) {
        Author author = getById(id);
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setBirthDate(request.getBirthDate());
        return authorRepo.save(author);
    }

    @Override
    public void delete(Long id) {
        Author author = getById(id);
        author.setDeleted(true);
        authorRepo.save(author);
    }
}
