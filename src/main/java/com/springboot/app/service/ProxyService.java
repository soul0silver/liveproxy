package com.springboot.app.service;

import com.springboot.app.dto.ProxyRequest;
import com.springboot.app.entity.Proxy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProxyService {
    Page<Proxy> list(ProxyRequest request);
    List<String> getRandom(ProxyRequest request);
    void scheduleRotate();
    void deleteSchedule();
    void changeIp();
}
