package com.hotel.hotel_management.web;

import com.hotel.hotel_management.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> processMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = chatbotService.processMessage(message);

        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("response", response);
        result.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/greet")
    public ResponseEntity<?> greet() {
        Map<String, String> greeting = new HashMap<>();
        greeting.put("response", "👋 Hello! Welcome to our hotel chatbot! How can I help you today?");
        return ResponseEntity.ok(greeting);
    }
}

