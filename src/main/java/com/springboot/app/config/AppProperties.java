package com.springboot.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private int schedule;
    private String urlListOrder;
    private String urlLoginPayos;
    private String emailPayos;
    private String passPayos;
    private String xApiKey;
    private String proxyControlUrl;
    private String listPath;
    private String changeIpPath;
    private String rotatePath;
    private List<String> deviceIds;
    private String remotePortPath;
}
