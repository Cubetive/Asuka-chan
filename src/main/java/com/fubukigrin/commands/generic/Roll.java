package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import com.fubukigrin.commands.BaseCommand;

import java.util.Random;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Roll extends BaseCommand {
    Roll() {
        super(
                "roll",
                "generic",
                "Roll a number between 1 and the specified limit, if applicable. The default limit is 100",
                "");
        
        addArgs(OptionType.INTEGER, "limit", "The specified limit", false);
    }

    @SuppressWarnings("null")
    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        String message = "";
        int limit = (event.getOption("limit") != null) ? event.getOption("limit").getAsInt() : 100;

        int number = rollNum(limit);
        message += event.getUser().getAsMention() + " rolled **" + String.valueOf(number) + "**";

        event.reply(message).queue();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String message = "";
        int limit;

        try {
            limit = (args.length > 1) ? Integer.parseInt(args[0]) : 100;
        } catch (Exception e) {
            // Ignore if user doesn't input an actual limit
            limit = 100;
        }

        int number = rollNum(limit);
        message += event.getAuthor().getAsMention() + " rolled **" + String.valueOf(number) + "**";

        event.getChannel().sendMessage(message).queue();
    }

    private int rollNum(int limit) {
        // Check if number is less than 2, which defaults it back to 100 if it's the
        // case
        if (limit < 2)
            limit = 100;

        // Pseudo random number generator
        Random random = new Random();
        return random.nextInt(limit) + 1;
    }
}
