// src/main/java/com/codesync/codesync/controller/EditorController.java
package com.codesync.codesync.controller;

import com.codesync.codesync.Dto.CursorMessage;
import com.codesync.codesync.Dto.EditorMessage;
import com.codesync.codesync.Dto.LanguageChangeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class EditorController {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    // 2. Create a constructor so Spring can inject the template
    public EditorController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // This is your existing code for real-time editor updates
    @MessageMapping("/editor.update/{sessionId}")
    @SendTo("/topic/editor/{sessionId}")
    public EditorMessage updateEditor(EditorMessage message) {
        return message;
    }

    // This is your existing code for real-time cursor updates
    @MessageMapping("/cursor.move/{sessionId}")
    @SendTo("/topic/cursor/{sessionId}")
    public CursorMessage moveCursor(CursorMessage message) {
        return message;
    }


    @MessageMapping("/language.change/{sessionId}")
    @SendTo("/topic/language/{sessionId}")
    public LanguageChangeMessage changeLanguage(LanguageChangeMessage message) {
        return message;
    }

    @MessageMapping("/session.admit/{sessionId}")
    public void admitCandidate(@DestinationVariable String sessionId) {
        // Use the messagingTemplate to send a message to a specific topic
        messagingTemplate.convertAndSend("/topic/session-control/" + sessionId, Map.of("type", "SESSION_STARTED"));
    }
}