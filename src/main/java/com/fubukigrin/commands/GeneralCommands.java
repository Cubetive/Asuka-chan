package com.fubukigrin.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GeneralCommands {
    public static void ping(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }
}
