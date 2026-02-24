package com.example.springai.controller;

import com.example.springai.service.ChatClientService;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {


    private final ChatClientService chatClientService;

    public ChatController(ChatClientService chatClientService) {
        this.chatClientService = chatClientService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring AI!";
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        return chatClientService.sendMessage(message);
    }

    @GetMapping("/authors/{author}")
    public Map<String,Object> authors(@PathVariable String author) {
        String message = """
    Generate list of links for the author {author}.
    Include the author name as the key and social links as the value.
    If unknown, say "I don't know".
    {format}
    """;
        var map = new MapOutputConverter();
        var pt  = new PromptTemplate(message);
        var p   = pt.create(Map.of("author", author, "format", map.getFormat()));

        return chatClientService.messageMapResponse(p);
    }

}