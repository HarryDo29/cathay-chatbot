package com.cathay.chatbot.config;

import com.google.genai.Client;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleGenAIConfig {
    @Value("${gemini_api_key}")
    private String apiKey;



    @Bean
    public Client client(){
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public GoogleGenAiChatModel googleGenAiChatModel(Client client){
        GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
                .model("gemini-3-flash-preview")
                .temperature(1.0)
                .build();
        return GoogleGenAiChatModel.builder()
                .genAiClient(client)
                .defaultOptions(options)
                .build();
    }
}
