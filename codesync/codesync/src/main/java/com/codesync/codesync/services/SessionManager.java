// src/main/java/com/codesync/project/service/SessionManager.java
package com.codesync.codesync.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// A simple 'record' to hold the two tokens for a session.
// Records are a modern Java feature for simple data classes.
record SessionDetails(String interviewerToken, String candidateToken) {}

@Service // This annotation tells Spring that this is a service class
public class SessionManager {

    // These maps will store our session data in memory.
    // Using ConcurrentHashMap makes them safe for multi-threaded environments.
    private final Map<String, SessionDetails> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToRoleMap = new ConcurrentHashMap<>();

    /**
     * Creates a new session with a unique ID and unique tokens for the
     * interviewer and candidate.
     * @return A map containing the new session's ID and tokens.
     */
    public Map<String, String> createSession() {
        String sessionId = UUID.randomUUID().toString();
        String interviewerToken = UUID.randomUUID().toString();
        String candidateToken = UUID.randomUUID().toString();

        // Store the details for this session
        activeSessions.put(sessionId, new SessionDetails(interviewerToken, candidateToken));
        
        // Map each token to its role for easy lookup later
        tokenToRoleMap.put(interviewerToken, "INTERVIEWER");
        tokenToRoleMap.put(candidateToken, "CANDIDATE");

        // Return all the details to the controller
        return Map.of(
            "sessionId", sessionId,
            "interviewerToken", interviewerToken,
            "candidateToken", candidateToken
        );
    }

    /**
     * Looks up the role for a given token.
     * @param token The token to check.
     * @return The role ("INTERVIEWER" or "CANDIDATE") or null if the token is invalid.
     */
    public String getRoleForToken(String token) {
        return tokenToRoleMap.get(token);
    }
}