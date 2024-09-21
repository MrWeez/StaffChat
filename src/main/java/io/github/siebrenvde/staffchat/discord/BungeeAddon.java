package io.github.siebrenvde.staffchat.discord;

import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.DiscordBotCommand;
import io.github.siebrenvde.staffchat.Bungee;
import io.github.siebrenvde.staffchat.util.BungeeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class BungeeAddon extends SimpleAddon {

    private Bungee plugin;
    private static BungeeAddon instance;
    private DiscordBot bot;
    public String prefix;

    public BungeeAddon(Bungee pl) {
        super("StaffChat","staffchat","Siebrenvde");
        instance = this;
        plugin = pl;
    }

    @Override
    public void onReady(DiscordBot bot) {
        this.bot = bot;
        prefix = bot.getCommandPrefix();
        enableCommands();
        bot.getJda().addEventListener(new MessageListenerBungee(plugin));
    }

    private void enableCommands() {
        if(plugin.config.getBoolean("enable-discord-commands")) {
            bot.onCommand("sc", this::staffChat);
            bot.onCommand("staffchat", this::staffChat);
            bot.onCommand("schat", this::staffChat);
            bot.onCommand("staffc", this::staffChat);
        }
    }

    public void sendMessage(String message) {
        TextChannel tc = bot.getJda().getTextChannelById(plugin.config.getString("staff-channel"));
        tc.sendMessage(message).queue();
    }

    public void sendEmbed(String title, String description) {
        TextChannel tc = bot.getJda().getTextChannelById(plugin.config.getString("staff-channel"));
        tc.sendMessageEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).build()).queue();
    }

    private void staffChat(DiscordBotCommand command) {
        User user = command.getMessage().getAuthor();
        String msg = command.getMessage().getContentRaw();
        if(msg.split(" ").length == 1) {
            command.reply("**Использование**: `" + prefix + command.getName() + " <сообщение>`");
        } else {
            BungeeUtils.sendPermissionMessage(plugin.minecraftLayout(msg, user), "staffchat.see");
        }
    }

}
