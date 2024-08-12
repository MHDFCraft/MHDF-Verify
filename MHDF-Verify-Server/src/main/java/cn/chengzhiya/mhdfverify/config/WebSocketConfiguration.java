package cn.chengzhiya.mhdfverify.config;

import cn.chengzhiya.mhdfverify.server.WebSocket;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocket(), "/ws").setAllowedOrigins("*");
    }
}
