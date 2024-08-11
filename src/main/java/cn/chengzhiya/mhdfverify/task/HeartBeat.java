package cn.chengzhiya.mhdfverify.task;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import static cn.chengzhiya.mhdfverify.server.WebSocket.send;

@Controller
@Component
public final class HeartBeat {
    @Scheduled(cron = "* * * * * ?")
    public void run() {
        {
            JSONObject data = new JSONObject();
            data.put("action", "heartBeat");

            send(data.toJSONString());
        }
    }
}
