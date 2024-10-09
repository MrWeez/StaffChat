package io.github.siebrenvde.staffchat;

import io.github.siebrenvde.staffchat.commands.spigot.HelpOp;
import io.github.siebrenvde.staffchat.commands.spigot.Report;
import io.github.siebrenvde.staffchat.commands.spigot.StaffChat;
import io.github.siebrenvde.staffchat.discord.SpigotAddon;
import io.github.siebrenvde.staffchat.events.SpigotMessageEvent;
import io.github.siebrenvde.staffchat.util.SpigotUtils;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spicord.SpicordLoader;

import java.util.ArrayList;
import java.util.List;

public class Spigot extends JavaPlugin {

    public SpigotAddon addon;

    public List<Player> toggledPlayers;

    public void onEnable() {
        addon = new SpigotAddon(this);
        toggledPlayers = new ArrayList<>();
        saveDefaultConfig();
        registerCommands();
        getServer().getPluginManager().registerEvents(new SpigotMessageEvent(this), this);
        SpicordLoader.addStartupListener(spicord -> {
            spicord.getAddonManager().registerAddon(addon);
        });
    }

    private void registerCommands() {
        getCommand("staffchat").setExecutor(new StaffChat(this));
        getCommand("report").setExecutor(new Report(this));
        getCommand("helpop").setExecutor(new HelpOp(this));
    }

    private FileConfiguration config = getConfig();

    public String generalLayout(String msg, String player, String playerDN) {
        return SpigotUtils.translateCC(config.getString("general-layout")
                .replace("%displayname%", playerDN)
                .replace("%username%", player)
                .replace("%message%", msg));
    }

    public String minecraftLayout(String msg, User user) {

        String p = addon.prefix;
        String dscMsg = msg.replaceFirst(p + "sc ", "").replaceFirst(p + "staffchat ", "").replaceFirst(p + "schat ", "").replaceFirst(p + "staffc ", "");

        return SpigotUtils.translateCC(config.getString("minecraft-layout")
                .replace("%username%", user.getEffectiveName())
                .replace("%usertag%", user.getAsTag())
                .replace("%message%", dscMsg));
    }

    public String discordLayout(String msg, String player, String playerDN) {
        return config.getString("discord-layout")
                .replace("%displayname%", playerDN)
                .replace("%username%", player)
                .replace("%message%", SpigotUtils.removeCC(msg));
    }

    public String rmdLayout(String msg, String reporter, String reported) {
        return config.getString("report-message-discord")
                .replace("%reporter%", reporter)
                .replace("%reported%", reported)
                .replace("%reason%", SpigotUtils.removeCC(msg));
    }

    public String rmLayout(String msg, String reporter, String reported) {
        return SpigotUtils.translateCC(config.getString("report-message")
                .replace("%reporter%", reporter)
                .replace("%reported%", reported)
                .replace("%reason%", msg));
    }

    public String homdLayout(String msg, String player) {
        return config.getString("helpop-message-discord")
                .replace("%player%", player)
                .replace("%message%", SpigotUtils.removeCC(msg));
    }

    public String homLayout(String msg, String player) {
        return SpigotUtils.translateCC(config.getString("helpop-message")
                .replace("%player%", player)
                .replace("%message%", msg));
    }

}
