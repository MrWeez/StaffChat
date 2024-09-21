package io.github.siebrenvde.staffchat.commands.bungee;

import io.github.siebrenvde.staffchat.Bungee;
import io.github.siebrenvde.staffchat.discord.BungeeAddon;
import io.github.siebrenvde.staffchat.util.BungeeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ChatColor;

public class StaffChat extends Command {

    private Bungee plugin;
    private BungeeAddon addon;

    public StaffChat(Bungee pl) {
        super("staffchat", "", "sc", "staffc", "schat");
        plugin = pl;
        addon = pl.addon;
    }

    public void execute(CommandSender sender, String[] strings) {

        String msg = String.join(" ", strings);

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if(player.hasPermission("staffchat.use")) {

                if(strings.length == 0) {
                    player.sendMessage(new TextComponent(ChatColor.RED + "Использование: /staffchat <сообщение>"));
                }

                else if(strings.length == 1 && strings[0].equalsIgnoreCase("toggle")) {
                    if(plugin.toggledPlayers.contains(player)) {
                        plugin.toggledPlayers.remove(player);
                        player.sendMessage(new TextComponent(ChatColor.RED + "Стафф-чат выключен"));
                    } else {
                        plugin.toggledPlayers.add(player);
                        player.sendMessage(new TextComponent(ChatColor.GREEN + "Стафф-чат включён"));
                    }
                }

                else {
                    String server = player.getServer().getInfo().getName();
                    BungeeUtils.sendPermissionMessage(plugin.generalLayout(msg, player.getName(), player.getDisplayName(), server),"staffchat.see");
                    addon.sendMessage(plugin.discordLayout(msg, player.getName(), player.getDisplayName(), server));
                }

            }

            else{ player.sendMessage(new TextComponent(BungeeUtils.permissionMessage)); }

        }

        else {

            if(strings.length == 0) {
                plugin.getLogger().info(ChatColor.RED + "Использование: /staffchat <сообщение>");
            } else {
                BungeeUtils.sendPermissionMessage(plugin.generalLayout(msg, "CONSOLE", "CONSOLE", "CONSOLE"), "staffchat.see");
                addon.sendMessage(plugin.discordLayout(msg, "CONSOLE", "CONSOLE", "CONSOLE"));
            }

        }

    }

}
