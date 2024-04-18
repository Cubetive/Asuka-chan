package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping {
    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }

    public static void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage("Pong!").queue();
    }
}
