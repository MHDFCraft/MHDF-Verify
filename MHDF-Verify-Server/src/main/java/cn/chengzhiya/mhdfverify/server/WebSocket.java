package cn.chengzhiya.mhdfverify.server;

import cn.chengzhiya.mhdfverify.util.LogUtil;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public final class WebSocket extends TextWebSocketHandler {
    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final HashMap<String, List<WebSocketSession>> sessionMap = new HashMap<>();

    public static List<WebSocketSession> getOnlineSessions(String credential) {
        if (sessionMap.get(credential) != null) {
            return sessionMap.get(credential);
        } else {
            return new ArrayList<>();
        }
    }

    public static Set<String> getOnlineClients(String credential) {
        List<WebSocketSession> onlineSessions = getOnlineSessions(credential);
        Set<String> onlineClients = new HashSet<>();
        onlineSessions.forEach(session -> onlineClients.add(Objects.requireNonNull(session.getRemoteAddress()).getHostString()));
        return onlineClients;
    }

    public static void send(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                sessions.remove(session);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String credential = session.getHandshakeHeaders().getFirst("credential");
        if (session.getRemoteAddress() != null) {
            String ip = session.getRemoteAddress().getHostString();
            if (credential != null) {
                open(session, credential);
                LogUtil.log("", "", ip, credential + "已连接WebSocket服务器,当前客户端数量:" + getOnlineSessions(credential).size() + ",当前设备数量:" + getOnlineClients(credential).size());
            } else {
                LogUtil.log("", "", ip, "未提供登录凭证!");
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未提供登录凭证"));
            }
        } else {
            LogUtil.log("", "", "", "未提供登录凭证!");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("未提供IP数据"));
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (status.getCode() != 1003) {
            String credential = session.getHandshakeHeaders().getFirst("credential");
            String ip = Objects.requireNonNull(session.getRemoteAddress()).getHostString();
            close(session, credential);
            LogUtil.log("", "", ip, credential + "已断开WebSocket服务器,当前客户端数量:" + getOnlineSessions(credential).size() + ",当前设备数量:" + getOnlineClients(credential).size());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, @NonNull Throwable exception) {
        String credential = session.getHandshakeHeaders().getFirst("credential");
        String ip = Objects.requireNonNull(session.getRemoteAddress()).getHostString();
        close(session, credential);
        LogUtil.log("", "", ip, credential + "已断开WebSocket服务器,当前客户端数量:" + getOnlineSessions(credential).size() + ",当前设备数量:" + getOnlineClients(credential).size());
    }

    private void open(WebSocketSession session, String credential) {
        sessions.add(session);

        List<WebSocketSession> onlineList = getOnlineSessions(credential);
        onlineList.add(session);
        sessionMap.put(credential, onlineList);
    }

    private void close(WebSocketSession session, String credential) {
        sessions.remove(session);

        List<WebSocketSession> onlineList = getOnlineSessions(credential);
        onlineList.remove(session);
        sessionMap.put(credential, onlineList);
    }
}
