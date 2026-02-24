package com.example.demo.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionStore {
    private final Map<String, String> activeSessions = new HashMap<>();

    public void addSession(String sessionId, String email) {
        activeSessions.put(sessionId, email);
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }
}
