package com.cathay.chatbot.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OllamaConfig {
    // 1. Khởi tạo kết nối đến Ollama Server (Localhost)
    @Bean
    public OllamaApi ollamaApi() {
        return OllamaApi.builder()
                .baseUrl("http://localhost:11434") // Mặc định Ollama chạy ở cổng 11434
                .build();
    }

    // 2. Tạo Bean cho Ollama Chat Model
    @Bean
    public OllamaChatModel ollamaChatModel(OllamaApi ollamaApi) {
        OllamaChatOptions options = OllamaChatOptions.builder()
                .model("llama3-vn")
                .temperature(1.0)
                .build();
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

    // 3. Cấu hình Embedding Model (Thay thế cho text-embedding-004)
    // Dùng để tạo vector cho PGVector
    @Bean
    public EmbeddingModel embeddingModel(OllamaApi ollamaApi) {
        OllamaEmbeddingOptions options = OllamaEmbeddingOptions.builder()
                .model("nomic-embed-text")
                .build();
        return OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }
}


