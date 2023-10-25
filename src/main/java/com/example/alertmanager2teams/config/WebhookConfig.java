package com.example.alertmanager2teams.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "teams.webhooks")
public class WebhookConfig {

    private Map<String, Set<String>> alertMap;

    private Map<String, String> webhookMap;

    public String lookupAlert(String alert) {

        if (webhookMap == null || webhookMap.isEmpty()) {
            webhookMap = new HashMap<>();
            alertMap.entrySet().forEach(entry -> {
                entry.getValue().forEach(entryValue -> {
                    webhookMap.put(entryValue, entry.getKey());
                });
            });
        }

        return webhookMap.get(alert);
    }
}
