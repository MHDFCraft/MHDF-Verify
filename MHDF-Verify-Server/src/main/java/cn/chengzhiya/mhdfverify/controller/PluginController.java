package cn.chengzhiya.mhdfverify.controller;

import cn.chengzhiya.mhdfverify.entity.Plugin;
import cn.chengzhiya.mhdfverify.entity.User;
import cn.chengzhiya.mhdfverify.entity.dto.VerifyDto;
import cn.chengzhiya.mhdfverify.server.WebSocket;
import cn.chengzhiya.mhdfverify.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import static cn.chengzhiya.mhdfverify.util.Sha256Util.sha256;

@RestController
@RequestMapping("/plugin")
public class PluginController extends ABaseController {
    @GetMapping("/version")
    public response getVersion(String pluginName) {
        if (pluginName != null) {
            Plugin plugin = PluginUtil.getPlugin(pluginName);
            if (plugin != null) {
                return success(plugin.getPluginVersion());
            } else {
                return error("不存在这个插件!");
            }
        } else {
            return error("必须要传递'pluginName'参数!");
        }
    }

    @PostMapping("/verify")
    public response verify(HttpServletRequest request, @RequestBody(required = false) VerifyDto verifyDto) throws Exception {
        String ip = request.getRemoteAddr();
        if (verifyDto != null && verifyDto.getUserName() != null) {
            String userName = RSAUtil.decode(Base64Util.decode(verifyDto.getUserName().replaceAll(" ", "+")));
            if (verifyDto.getPassword() != null) {
                String password = RSAUtil.decode(Base64Util.decode(verifyDto.getPassword().replaceAll(" ", "+")));
                if (verifyDto.getPluginName() != null) {
                    User user = UserUtil.getUser(userName);
                    if (user != null) {
                        if (user.getPassword().equals(password)) {
                            Plugin plugin = PluginUtil.getPlugin(verifyDto.getPluginName());
                            if (plugin != null) {
                                if (PluginUtil.verifyPlugin(user, plugin)) {
                                    if (plugin.getMaxClient() <= -1 || WebSocket.getOnlineClients(ip).size() <= plugin.getMaxClient()) {
                                        String wsLoginCredential = sha256(userName + password + verifyDto.getPluginName());
                                        LogUtil.log(userName, verifyDto.getPluginName(), ip, "验证成功 WS授权登录凭证: " + wsLoginCredential);
                                        return success(wsLoginCredential);
                                    } else {
                                        LogUtil.log(userName, verifyDto.getPluginName(), ip, "超出设备上线");
                                        return error("超出设备上线");
                                    }
                                } else {
                                    LogUtil.log(userName, verifyDto.getPluginName(), ip, "验证失败");
                                    return error("验证失败");
                                }
                            } else {
                                LogUtil.log(userName, verifyDto.getPluginName(), ip, "插件不存在");
                                return error("不存在这个插件!");
                            }
                        } else {
                            LogUtil.log(userName, verifyDto.getPluginName(), ip, "账户密码错误");
                            return error("账户密码错误!");
                        }
                    } else {
                        LogUtil.log(userName, verifyDto.getPluginName(), ip, "账户不存在");
                        return error("不存在这个用户!");
                    }
                } else {
                    LogUtil.log(userName, "", ip, "未传递'pluginName'参数");
                    return error("必须要传递'pluginName'参数!");
                }
            } else {
                LogUtil.log(userName, "", ip, "未传递'password'参数");
                return error("必须要传递'password'参数!");
            }
        } else {
            LogUtil.log("", "", ip, "未传递'userName'参数");
            return error("必须要传递'userName'参数!");
        }
    }
}
