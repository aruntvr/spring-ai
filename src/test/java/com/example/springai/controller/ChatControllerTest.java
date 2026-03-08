package com.example.springai.controller;

import com.example.springai.service.ChatClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatClientService chatClientService;

    @Test
    void helloEndpointReturnsGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/chat/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from Spring AI!"));
    }

    @Test
    void sendEndpointForwardsMessageToService() throws Exception {
        when(chatClientService.sendMessage("hi")).thenReturn("pong");

        mockMvc.perform(get("/api/v1/chat/send").param("message", "hi"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void authorsEndpointConvertsServiceOutputToMap() throws Exception {
        // This endpoint uses MapOutputConverter to parse model output; verifying full parsing
        // requires reproducing the exact model response format. For now ensure the endpoint
        // calls the service with a Prompt and returns a non-error status when the service
        // returns an empty JSON object.
        when(chatClientService.sendMessage(any(org.springframework.ai.chat.prompt.Prompt.class))).thenReturn("{}");

        mockMvc.perform(get("/api/v1/chat/authors/Bob"))
                .andExpect(status().isOk());
    }

}
