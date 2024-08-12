package cn.chengzhiya.example.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public final class LogUtil {
    public static void colorLog(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
    }
}
