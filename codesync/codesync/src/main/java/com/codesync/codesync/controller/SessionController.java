package com.codesync.codesync.controller;

import com.codesync.codesync.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*") // Allows requests from any origin (e.g., your React app)
public class SessionController {

    @Autowired
    private final SessionManager sessionManager;
    public SessionController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    @PostMapping("/create")
    public Map<String, String> createSession() {
        // 4. Delegate the work to the service
        return sessionManager.createSession();
    }
}