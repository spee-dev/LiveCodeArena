
package com.codesync.codesync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // --- CHANGE 1: Enable the "/queue" prefix for private messages ---
        registry.enableSimpleBroker("/topic", "/queue");
    }

    // --- CHANGE 2: Add the new Channel Interceptor method ---
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Check if it's a new connection command
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Get the custom headers sent by the frontend client
                    List<String> sessionIdHeader = accessor.getNativeHeader("sessionId");
                    List<String> tokenHeader = accessor.getNativeHeader("roleToken");

                    // Put them into the WebSocket session attributes to be used later
                    if (sessionIdHeader != null && !sessionIdHeader.isEmpty()) {
                        accessor.getSessionAttributes().put("sessionId", sessionIdHeader.get(0));
                    }
                    if (tokenHeader != null && !tokenHeader.isEmpty()) {
                        accessor.getSessionAttributes().put("roleToken", tokenHeader.get(0));
                    }
                }
                return message;
            }
        });
    }
}