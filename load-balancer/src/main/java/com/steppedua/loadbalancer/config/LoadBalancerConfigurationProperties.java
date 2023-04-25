package com.steppedua.loadbalancer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "vote.server")
public class LoadBalancerConfigurationProperties {
    private Map<Integer, String> serversIp;
    private Integer serverQuantity;
    private String serverPath;
}
