package com.example.alertmanager2teams;

import com.example.alertmanager2teams.config.WebhookConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebhookConfig.class)
public class MsTeamsApplication {

    public static void main(String[] args) {

        SpringApplication.run(MsTeamsApplication.class, args);
    }

}
