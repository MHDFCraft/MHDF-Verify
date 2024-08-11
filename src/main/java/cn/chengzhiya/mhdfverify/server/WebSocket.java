package cn.chengzhiya.mhdfverify.server;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint("/ws/{mac}")
public class WebSocket {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static final HashMap<String, List<Session>> sessionMap = new HashMap<>();

    public static List<Session> getOnlineSessions(String ip) {
        if (sessionMap.get(ip) != null) {
            return sessionMap.get(ip);
        } else {
            return new ArrayList<>();
        }
    }

    public static void send(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                sessions.remove(session);
                throw new RuntimeException(e);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("mac") String mac) {
        log.info("客户端{}({})连接至websocket服务端!", session.getId(), session.getRequestURI().toString());
        open(session, mac);
    }

    @OnClose
    public void onClose(Session session, @PathParam("mac") String mac) {
        log.info("客户端{}({})断开websocket服务端!", session.getId(), session.getRequestURI().toString());
        close(session, mac);
    }

    @OnMessage
    public void onMessage() {
    }

    @OnError
    public void onError(Session session, @PathParam("mac") String mac) {
        log.info("客户端{}({})断开websocket服务端!", session.getId(), session.getRequestURI().toString());
        close(session, mac);
    }

    private void open(Session session, String mac) {
        sessions.add(session);
        List<Session> onlineList = getOnlineSessions(mac);
        onlineList.add(session);
        sessionMap.put(mac, onlineList);
    }

    private void close(Session session, String mac) {
        sessions.remove(session);
        List<Session> onlineList = getOnlineSessions(mac);
        onlineList.remove(session);
        sessionMap.put(mac, onlineList);
    }
}
