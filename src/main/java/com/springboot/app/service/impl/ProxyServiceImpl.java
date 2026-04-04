package com.springboot.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.config.AppProperties;
import com.springboot.app.dto.ProxyResponse;
import com.springboot.app.dto.UpdatePort;
import com.springboot.app.entity.InUsePort;
import com.springboot.app.dto.ProxyRequest;
import com.springboot.app.entity.Proxy;
import com.springboot.app.entity.User;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.InUseProxyRepo;
import com.springboot.app.repo.KeyRepo;
import com.springboot.app.repo.ProxyRepo;
import com.springboot.app.repo.UserRepo;
import com.springboot.app.repo.specification.ProxySpecification;
import com.springboot.app.service.BaseService;
import com.springboot.app.service.ProxyService;
import com.springboot.app.utils.ContextUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.springboot.app.service.BaseService.random;

@Service
@RequiredArgsConstructor
@Transactional
public class ProxyServiceImpl implements ProxyService {
    private final AppProperties appProperties;
    private final ProxyRepo proxyRepo;
    private final UserRepo userRepo;
    private final KeyRepo keyRepo;
    private final BaseService baseService;
    private final InUseProxyRepo inUseProxyRepo;
    private final RestTemplate restTemplate;

    @Override
    public Page<Proxy> list(ProxyRequest request) {
        Pageable pageable = PageRequest.of(0, 10);
        return proxyRepo.findAll(ProxySpecification.getSpecification(request), pageable);
    }

    @Override
    public List<String> getRandom(ProxyRequest request) {
        var uid = userRepo.findByUsername(ContextUtils.getCurrentUser())
                .map(User::getId)
                .orElseThrow(BusinessEx::new);
        var keys = keyRepo.findAllByCustomerIdAndKeyProxyIn(uid, request.getKeys());
        List<Proxy> result = proxyRepo.getListProxies(keys.size());
        List<InUsePort> inUsePorts = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            inUsePorts.add(new InUsePort(
                    keys.get(i).getKeyProxy(),
                    result.get(i).getHttpRemote().split(":")[1],
                    keys.get(i).getOutdated(),
                    result.get(i).getId().split("-")[0])
            );
        }

        inUseProxyRepo.saveAll(inUsePorts);
        baseService.updateProxy(result.stream().map(Proxy::getId).collect(Collectors.toList()));
        baseService.changePort(request.getKeys().stream().toList());
        return result.stream().map(Proxy::getHttpRemote).collect(Collectors.toList());
    }

//    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.MINUTES)
    void update() {
        ObjectMapper mapper = new ObjectMapper();
        List<Proxy> list = new ArrayList<>();
        appProperties.getDeviceIds().forEach(id -> {
            String url = appProperties.getProxyControlUrl() + appProperties.getListPath() + "/" + id;
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", appProperties.getXApiKey());
            HttpEntity entity = new HttpEntity<>(headers);
            var res = restTemplate.exchange(url, HttpMethod.GET, entity, ProxyResponse.class);
            List<Proxy> ps = mapper.convertValue(res.getBody().getData(), new TypeReference<List<Proxy>>() {
            });
            var proxies = Objects.requireNonNull(ps).stream()
                    .map(p -> {
                        p.setId(id + "-" + p.getId());
                        return p;
                    }).toList();
            list.addAll(proxies);
        });
        proxyRepo.saveAll(list);
    }

//    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.MINUTES)
    void autoChangePort() {
        List<InUsePort> list = inUseProxyRepo.list(50);
        list.parallelStream().forEach(proxy -> {
            String url = appProperties.getProxyControlUrl() + appProperties.getRemotePortPath() + "/" + proxy.getDeviceId();
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", appProperties.getXApiKey());
            HttpEntity entity = new HttpEntity<>(new UpdatePort(random(proxy.getPort()), proxy.getPort()), headers);
            restTemplate.exchange(url, HttpMethod.POST, entity, ProxyResponse.class);
        });
        inUseProxyRepo.deleteAll(list);
    }


    @Override
    public void scheduleRotate() {

    }

    @Override
    public void deleteSchedule() {

    }

    @Override
    public void changeIp() {

    }


}
