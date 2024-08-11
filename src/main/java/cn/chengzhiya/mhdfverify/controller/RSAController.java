package cn.chengzhiya.mhdfverify.controller;

import cn.chengzhiya.mhdfverify.utils.Base64Util;
import cn.chengzhiya.mhdfverify.utils.RSAUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rsa")
public class RSAController extends ABaseController {
    @GetMapping("/getPublic")
    public response getPublic() {
        return success(Base64Util.encode(RSAUtil.getPublicKey().getEncoded()));
    }

    @GetMapping("/encode")
    public response encode(String data) throws Exception {
        return success(Base64Util.encode(RSAUtil.encode(data)));
    }
}
