package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Ping {
    public static void test() {
        System.out.println("hi!");
    }

    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }
}
