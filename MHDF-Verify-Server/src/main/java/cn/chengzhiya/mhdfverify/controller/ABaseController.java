package cn.chengzhiya.mhdfverify.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public class ABaseController {
    public response success(Object data) {
        response response = new response();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public response error(Object msg) {
        response response = new response();
        response.setCode(500);
        response.setMsg("error");
        response.setData(msg);
        return response;
    }

    @Data
    public static class response {
        private Integer code;
        private String msg;
        private Object data;
    }
}
