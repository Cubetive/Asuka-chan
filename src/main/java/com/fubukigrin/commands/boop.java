package com.fubukigrin.commands;

import javax.annotation.Nonnull;
import java.util.Random;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class boop {
    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Boop!").queue();
    }
}
