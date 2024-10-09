package io.github.siebrenvde.staffchat;

import io.github.siebrenvde.staffchat.commands.bungee.HelpOp;
import io.github.siebrenvde.staffchat.commands.bungee.Report;
import io.github.siebrenvde.staffchat.commands.bungee.StaffChat;
import io.github.siebrenvde.staffchat.discord.BungeeAddon;
import io.github.siebrenvde.staffchat.events.BungeeMessageEvent;
import io.github.siebrenvde.staffchat.util.BungeeUtils;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.spicord.SpicordLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Bungee extends Plugin {

    public BungeeAddon addon;

    public List<ProxiedPlayer> toggledPlayers;

    public void onEnable() {
        addon = new BungeeAddon(this);
        toggledPlayers = new ArrayList<>();
        registerCommands();
        registerConfig();
        getProxy().getPluginManager().registerListener(this, new BungeeMessageEvent(this));
        SpicordLoader.addStartupListener(spicord -> {
            spicord.getAddonManager().registerAddon(addon);
        });
    }

    private void registerCommands(){
        getProxy().getPluginManager().registerCommand(this, new StaffChat(this));
        getProxy().getPluginManager().registerCommand(this, new Report(this));
        getProxy().getPluginManager().registerCommand(this, new HelpOp(this));
    }

    private File file;
    public Configuration config;

    private void registerConfig() {

        try {
            Files.createDirectories(Paths.get(ProxyServer.getInstance().getPluginsFolder() + "/StaffChat"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File(ProxyServer.getInstance().getPluginsFolder() + "/StaffChat/config.yml");

        try {
            if(!file.exists()) {
                Files.copy(getClass().getResourceAsStream("/proxyconfig.yml"), Paths.get(ProxyServer.getInstance().getPluginsFolder() + "/StaffChat/config.yml"));
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String generalLayout(String msg, String player, String playerDN, String server) {
        return BungeeUtils.translateCC(config.getString("general-layout")
                .replace("%displayname%", playerDN)
                .replace("%username%", player)
                .replace("%server%", server)
                .replace("%message%", msg));
    }

    public String minecraftLayout(String msg, User user) {

        String p = addon.prefix;
        String dscMsg = msg.replaceFirst(p + "sc ", "").replaceFirst(p + "staffchat ", "").replaceFirst(p + "schat ", "").replaceFirst(p + "staffc ", "");

        return BungeeUtils.translateCC(config.getString("minecraft-layout")
                .replace("%username%", user.getName())
                .replace("%usertag%", user.getAsTag())
                .replace("%message%", dscMsg));
    }

    public String discordLayout(String msg, String player, String playerDN, String server) {
        return config.getString("discord-layout")
                .replace("%displayname%", playerDN)
                .replace("%username%", player)
                .replace("%server%", server)
                .replace("%message%", BungeeUtils.removeCC(msg));
    }

    public String rmdLayout(String msg, String reporter, String reported, String server) {
        return config.getString("report-message-discord")
                .replace("%reporter%", reporter)
                .replace("%reported%", reported)
                .replace("%server%", server)
                .replace("%reason%", BungeeUtils.removeCC(msg));
    }

    public String rmLayout(String msg, String reporter, String reported, String server) {
        return BungeeUtils.translateCC(config.getString("report-message")
                .replace("%reporter%", reporter)
                .replace("%reported%", reported)
                .replace("%server%", server)
                .replace("%reason%", msg));
    }

    public String homdLayout(String msg, String player, String server) {
        return config.getString("helpop-message-discord")
                .replace("%player%", player)
                .replace("%server%", server)
                .replace("%message%", BungeeUtils.removeCC(msg));
    }

    public String homLayout(String msg, String player, String server) {
        return BungeeUtils.translateCC(config.getString("helpop-message")
                .replace("%player%", player)
                .replace("%server%", server)
                .replace("%message%", msg));
    }

}
