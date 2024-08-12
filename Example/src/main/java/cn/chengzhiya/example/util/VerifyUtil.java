package cn.chengzhiya.example.util;

import cn.chengzhiya.example.Example;
import cn.chengzhiya.example.client.WebSocket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static cn.chengzhiya.example.util.LogUtil.colorLog;
import static cn.chengzhiya.example.util.SpigotUtil.getPluginName;

public final class VerifyUtil {
    @Getter
    private static final String serverURL = "127.0.0.1:8888";
    public static WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    private static String wsLoginCredential;

    public static boolean verify(String user, String password) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://" + serverURL + "/plugin/verify").openConnection();

        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        JSONObject body = new JSONObject();
        {
            body.put("userName", Base64Util.encode(RSAUtil.encode(user)));
            body.put("password", Base64Util.encode(RSAUtil.encode(password)));
            body.put("pluginName", getPluginName());
        }

        OutputStream out = conn.getOutputStream();
        out.write(JSON.toJSONString(body).getBytes(StandardCharsets.UTF_8));
        out.close();

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                conn.disconnect();

                JSONObject data = JSON.parseObject(response.toString());
                if (data.get("msg").equals("success")) {
                    wsLoginCredential = data.getString("data");
                    return true;
                }
            }
        }
        return false;
    }

    public static void connectWebsocketServer() {
        try {
            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("credential", List.of(wsLoginCredential));
                }
            };

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().configurator(configurator).build();

            container.connectToServer(new WebSocket(), clientEndpointConfig, new URI("ws://" + serverURL + "/ws"));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            colorLog("&c无法正常连接至websocket服务端!");
            if (Bukkit.getPluginManager().getPlugin(getPluginName()) != null) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(Example.instance, VerifyUtil::connectWebsocketServer, 100L);
            }
        }
    }
}
