package com.banking.ai;

import com.banking.ai.config.BankingAiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BankingAiProperties.class)
public class BankingAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAssistantApplication.class, args);
    }
}
