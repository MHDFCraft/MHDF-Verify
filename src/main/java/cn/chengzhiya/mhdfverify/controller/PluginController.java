package cn.chengzhiya.mhdfverify.controller;

import cn.chengzhiya.mhdfverify.entity.Plugin;
import cn.chengzhiya.mhdfverify.entity.User;
import cn.chengzhiya.mhdfverify.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public response verify(HttpServletRequest request, String userName, String password, String pluginName) throws Exception {
        String ip = request.getRemoteAddr();
        if (userName != null) {
            if (password != null) {
                if (pluginName != null) {
                    userName = RSAUtil.decode(Base64Util.decode(userName.replaceAll(" ", "+")));
                    password = RSAUtil.decode(Base64Util.decode(password.replaceAll(" ", "+")));
                    User user = UserUtil.getUser(userName);
                    if (user != null) {
                        if (user.getPassword().equals(password)) {
                            Plugin plugin = PluginUtil.getPlugin(pluginName);
                            if (plugin != null) {
                                if (PluginUtil.verifyPlugin(user, plugin)) {
                                    LogUtil.log(userName, pluginName, ip, "验证成功");
                                    return success("验证成功");
                                } else {
                                    LogUtil.log(userName, pluginName, ip, "验证失败");
                                    return error("验证失败");
                                }
                            } else {
                                LogUtil.log(userName, pluginName, ip, "插件不存在");
                                return error("不存在这个插件!");
                            }
                        } else {
                            LogUtil.log(userName, pluginName, ip, "账户密码错误");
                            return error("账户密码错误!");
                        }
                    } else {
                        LogUtil.log(userName, pluginName, ip, "账户不存在");
                        return error("不存在这个用户!");
                    }
                } else {
                    LogUtil.log("", "", ip, "未传递'pluginName'参数");
                    return error("必须要传递'pluginName'参数!");
                }
            } else {
                LogUtil.log("", "", ip, "未传递'password'参数");
                return error("必须要传递'password'参数!");
            }
        } else {
            LogUtil.log("", "", ip, "未传递'userName'参数");
            return error("必须要传递'userName'参数!");
        }
    }
}
