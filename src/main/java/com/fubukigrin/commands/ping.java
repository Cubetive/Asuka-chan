package com.fubukigrin.commands;

import javax.annotation.Nonnull;
import java.util.Random;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ping {
    public static void test() {
        System.out.println("hi!");
    }

    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }
}
