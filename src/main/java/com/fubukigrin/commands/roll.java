package com.fubukigrin.commands;

import javax.annotation.Nonnull;
import java.util.Random;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class roll {
    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        String message = "";
        int limit = (event.getOption("limit") != null) ? event.getOption("limit").getAsInt() : 100;

        // Check if number is less than 2, which defaults it back to 100 if it's the case
        if (limit < 2) limit = 100;

        // Pseudo random number generator
        Random random = new Random();
        int number = random.nextInt(limit) + 1;

        message += event.getUser().getName() + " rolled **" + String.valueOf(number) + "**";

        event.reply(message).queue();
    }
}
