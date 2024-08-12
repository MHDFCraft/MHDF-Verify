package cn.chengzhiya.example.client;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import org.bukkit.Bukkit;

import static cn.chengzhiya.example.util.SpigotUtil.getPluginName;
import static cn.chengzhiya.example.util.VerifyUtil.connectWebsocketServer;

public final class WebSocket extends Endpoint {
    public static Session session;

    public static void send(String message) {
        try {
            if (WebSocket.session != null) {
                if (WebSocket.session.isOpen()) {
                    WebSocket.session.getAsyncRemote().sendText(message);
                } else {
                    WebSocket.session = null;
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        WebSocket.session = session;
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        WebSocket.session = null;
        if (Bukkit.getPluginManager().getPlugin(getPluginName()) != null) {
            connectWebsocketServer();
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        WebSocket.session = null;
    }
}
