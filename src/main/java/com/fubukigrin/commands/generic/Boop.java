package com.fubukigrin.commands.generic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import com.fubukigrin.commands.BaseCommand;
import com.fubukigrin.button.CustomButton;

public class Boop extends BaseCommand {

    Boop() {
        super(
                "boop",
                "generic",
                "Boop!",
                "");
    }

    public static List<CustomButton> buildButtons(int index) {
        List<CustomButton> buttons = new ArrayList<CustomButton>();
        buttons.add(
                new CustomButton("boop" + index, "Boop!", ButtonStyle.PRIMARY, false, null)
                        .addCallback((ButtonInteractionEvent event) -> {
                            String message = event.getUser().getAsMention() + " Boop!";
                            event.reply(message).queue();
                        }));
        return buttons;
    }

    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.getChannel().sendMessage("Boop!")
                .setActionRow(buildButtons(0))
                .queue();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String message = event.getAuthor().getAsMention() + " Boop";
        event.getChannel().sendMessage(message)
                .setActionRow(buildButtons(0))
                .queue();
    }
}
