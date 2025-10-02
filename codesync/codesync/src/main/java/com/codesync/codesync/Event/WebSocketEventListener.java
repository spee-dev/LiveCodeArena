// src/main/java/com/codesync/codesync/event/WebSocketEventListener.java
package com.codesync.codesync.Event;

import com.codesync.codesync.services.SessionManager;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Objects; // Import Objects for null-safe operations

@Component
public class WebSocketEventListener {

    private final SessionManager sessionManager;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(SessionManager sessionManager, SimpMessagingTemplate messagingTemplate) {
        this.sessionManager = sessionManager;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        // --- THIS IS THE FIX ---
        // First, get the attributes map and check if it's null before using it.
        Map<String, Object> sessionAttributes = headers.getSessionAttributes();
        if (sessionAttributes == null) {
            System.out.println("Could not retrieve session attributes. Aborting connection logic.");
            return; // Exit the method gracefully
        }

        // Now that we know sessionAttributes is not null, we can safely get values from it.
        String sessionId = (String) sessionAttributes.get("sessionId");
        String roleToken = (String) sessionAttributes.get("roleToken");
        String userStompSessionId = headers.getSessionId();

        if (sessionId != null && roleToken != null) {
            String role = sessionManager.getRoleForToken(roleToken);

            if (role != null) {
                if ("CANDIDATE".equals(role)) {
                    messagingTemplate.convertAndSend("/topic/session-control/" + sessionId, Map.of("type", "CANDIDATE_JOINED"));
                }

                messagingTemplate.convertAndSendToUser(userStompSessionId, "/queue/role", Map.of("role", role));
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Here you would add logic to notify the other user if someone leaves.
        System.out.println("User Disconnected: " + event.getSessionId());
    }
}