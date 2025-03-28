package com.springboot.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.config.AppProperties;
import com.springboot.app.dto.*;
import com.springboot.app.entity.InUsePort;
import com.springboot.app.entity.PayosAcc;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.InUseProxyRepo;
import com.springboot.app.repo.PayosRepo;
import com.springboot.app.repo.ProxyRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BaseService {
    private final ProxyRepo proxyRepo;
    private final PayosRepo payosRepo;
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final InUseProxyRepo inUseProxyRepo;

    @Async
    public void updateProxy(List<String> ids) {
        proxyRepo.setUpdated(ids);
    }

//    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void loginPayos() {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Login payos");
        var res = restTemplate.postForObject(appProperties.getUrlLoginPayos(),
                new LoginRequest(appProperties.getEmailPayos(), appProperties.getPassPayos()), PayosResponse.class);
        if (Objects.isNull(res)) {
            log.error("Can not login payos");
        }
        else {
            var payos = objectMapper.convertValue(res.getData(), PayosAccDto.class);

            payosRepo.findPayosAccByEmail(appProperties.getEmailPayos()).ifPresentOrElse(
                    p -> {
                        BeanUtils.copyProperties(payos, p);
                        payosRepo.save(p);
                    },
                    () -> {
                        var payosAcc = new PayosAcc();
                        BeanUtils.copyProperties(payos, payosAcc);
                        payosRepo.save(payosAcc);
                    }
            );

        }
    }

    @Async
    public void changePort(List<String> keys) {
        List<InUsePort> list = inUseProxyRepo.findAllByKeyIn(keys);
        list.parallelStream().forEach(proxy -> {
            String url = appProperties.getProxyControlUrl() + appProperties.getRemotePortPath() + "/" + proxy.getDeviceId();
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", appProperties.getXApiKey());
            HttpEntity entity = new HttpEntity<>(new UpdatePort(random(proxy.getPort()), proxy.getPort()), headers);
            restTemplate.exchange(url, HttpMethod.POST, entity, ProxyResponse.class);
        });
    }

    public static String random(String value) {
        var port = Integer.valueOf(value);
        Random random = new Random();
        int min = port > 30000 ? 30000 : port;
        int max = port < 30000 ? 30000 : 60000;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }
}
