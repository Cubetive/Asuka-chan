package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Boop {
    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Boop!").queue();
    }

    public static void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String message = event.getAuthor().getAsMention() + " Boop!";
        event.getChannel().sendMessage(message).queue();
    }
}
