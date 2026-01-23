package com.Prakhar.Ecommerce.Contoller;

import com.Prakhar.Ecommerce.Service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat/completions")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {

        String msg = body.get("message");

        if(msg == null || msg.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reply","Please enter a message."));
        }

        String reply = aiService.chatReply(msg);

        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
