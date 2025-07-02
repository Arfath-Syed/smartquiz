package org.arfath.smartquiz.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAIApiKey;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultSystem("""
                    You are an expert quiz generation assistant.
                    Provide multiple choice questions with four options: A, B, C, D.
                    Clearly specify the correct answer.
                    """)
                .build();
    }
}
