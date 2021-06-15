package io.github.siebrenvde.staffchat.discord;

import io.github.siebrenvde.staffchat.Spigot;
import io.github.siebrenvde.staffchat.util.SpigotUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListenerSpigot extends ListenerAdapter {

    private Spigot plugin;

    MessageListenerSpigot(Spigot pl) {
        plugin = pl;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        String msg = message.getContentDisplay();

        if(event.isFromType(ChannelType.TEXT)) {

            TextChannel channel = event.getTextChannel();

            if(channel.getId().equals(plugin.getConfig().getString("staff-channel"))) {

                if(!plugin.getConfig().getBoolean("enable-discord-commands")) {

                    if (!author.isBot()) {

                        SpigotUtils.sendPermissionMessage(plugin.minecraftLayout(msg, author), "staffchat.see");

                    }

                }

            }

        }

    }

}
