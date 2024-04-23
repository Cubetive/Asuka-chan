package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import com.fubukigrin.commands.BaseCommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends BaseCommand {

    Ping() {
        super(
                "ping",
                "generic",
                "Ping!",
                "");
    }

    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage("Pong!").queue();
    }
}
