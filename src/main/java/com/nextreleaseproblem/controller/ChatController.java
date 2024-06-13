package com.nextreleaseproblem.controller;

import com.nextreleaseproblem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<String> chatWithGpt(@RequestBody String userMessage) {
        String response = chatService.getChatGptResponse(userMessage);
        return ResponseEntity.ok(response);
    }
}