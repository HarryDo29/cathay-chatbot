package com.cathay.chatbot.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
@AllArgsConstructor
public class ChatBotController {

    private final GoogleGenAiChatModel geminiModel;
    private final OllamaChatModel ollamaModel;

    @GetMapping("/gemini")
    public String chatWithGemini(){
        return geminiModel.call(
                new Prompt("Giới thiệu cho tôi 1 chút về Việt Nam")
        ).getResults().getFirst().getOutput().getText();
    }

    @GetMapping("/ollama")
    public String chatWithOllama(){
        return ollamaModel.call(
                new Prompt("Giới thiệu cho tôi 1 chút về Việt Nam")
        ).getResults().getFirst().getOutput().getText();
    }


}
