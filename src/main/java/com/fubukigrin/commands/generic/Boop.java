package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.fubukigrin.commands.BaseCommand;

public class Boop extends BaseCommand {

    Boop() {
        super(
                "boop",
                "generic",
                "Boop!",
                "",
                "");
    }

    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("Boop!").queue();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String message = event.getAuthor().getAsMention() + " Boop!";
        event.getChannel().sendMessage(message).queue();
    }
}
