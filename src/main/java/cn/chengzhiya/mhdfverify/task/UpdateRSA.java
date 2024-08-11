package cn.chengzhiya.mhdfverify.task;

import cn.chengzhiya.mhdfverify.utils.RSAUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.security.NoSuchAlgorithmException;

@Controller
@Component
public final class UpdateRSA {
    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() throws NoSuchAlgorithmException {
        RSAUtil.initRSAKeyPair();
    }
}
