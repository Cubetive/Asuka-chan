package com.fubukigrin.listeners;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter{
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("EventListener ready!");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event){
        User user = event.getAuthor();
        String content = event.getMessage().getContentDisplay();
        String message = null;

        if (user.isBot()) return; // Ignore all bot messages

        // Debug
        System.out.println(user.getName() + " said '" + content + "' in " + event.getChannel().getName());

        switch (content.toLowerCase()) {
            case "hello":
                message = "hi " + user.getName();
                break;
            case "owo":
                message = "uwu";
                break;
            case "dan dan":
                message = "Kikoeru";
                break;
            default:
                break;
        }

        if (event.isFromGuild()) {
            if (message != null) event.getGuild().getDefaultChannel().sendMessage(message).queue();
        }
        else if (event.isFromType(ChannelType.PRIVATE))
        {
            if (message != null) sendDirectMessageResponse(user, message);
        }
    }

    public void sendDirectMessageResponse(User user, String message) {
        user.openPrivateChannel()
            .flatMap(channel -> channel.sendMessage(message))
            .queue();
    }
}