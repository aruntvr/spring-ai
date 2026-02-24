package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class ChatClientService {

    private final ChatClient chatClient;

    public ChatClientService(OllamaChatModel chatModel, ChatMemory chatMemory) {
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        this.chatClient = ChatClient.builder(Objects.requireNonNull(chatModel))
                .defaultAdvisors(memoryAdvisor, new SimpleLoggerAdvisor())
                .defaultOptions(OllamaChatOptions.builder().temperature(0.3).build())
                .build();

    }

    public String sendMessage(String message) {
        return this.chatClient.prompt(message).call().content();
    }

    public Map<String, Object> messageMapResponse(Prompt message) {
        MapOutputConverter converter = new MapOutputConverter();
        return converter.convert(Objects.requireNonNull(this.chatClient.prompt(message).call().content()));
    }

}
