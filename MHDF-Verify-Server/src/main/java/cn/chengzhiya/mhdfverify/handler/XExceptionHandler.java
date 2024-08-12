package cn.chengzhiya.mhdfverify.handler;

import cn.chengzhiya.mhdfverify.controller.ABaseController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

@ControllerAdvice
@Slf4j
public final class XExceptionHandler extends ABaseController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public response AllExceptionHandler(HttpServletRequest req, Exception e) {
        if (e instanceof NoResourceFoundException) {
            return error("API接口或资源地址错误!");
        }
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return error("请求方式错误!");
        }
        if (e instanceof BadPaddingException) {
            return error("登录过期!");
        }
        if (e instanceof HttpMessageNotReadableException) {
            return error("传参错误!");
        }
        if (e instanceof IllegalBlockSizeException) {
            return error("RSA解码错误!");
        }
        log.error("\n运行错误-未知错误 [原因: ${}] StackTrace ↓", e.getMessage(), e);
        return error(e.getMessage());
    }
}
