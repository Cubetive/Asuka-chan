package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import com.fubukigrin.commands.BaseCommand;
import com.fubukigrin.button.ButtonManager;
import com.fubukigrin.button.CustomButton;

public class Boop extends BaseCommand {

    Boop() {
        super(
                "boop",
                "generic",
                "Boop!",
                "");
    }

    public static ButtonManager buildButtons(int index) {
        ButtonManager buttons = new ButtonManager();
        buttons.setTimeoutTime(2);

        buttons.add(
                new CustomButton("boop" + index, "Boop!", ButtonStyle.PRIMARY, false, null)
                        .addCallback((ButtonInteractionEvent event) -> {
                            String message = event.getUser().getAsMention() + " Boop!";
                            event.reply(message).queue();
                        }));
        return buttons;
    }

    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        ButtonManager buttonManager = buildButtons(0)
                .setJda(event.getJDA())
                .setTimeoutCallback((ButtonManager m) -> {
                    System.out.println("Timeout!");
                    m.disableAll();
                    event.getHook().editOriginalComponents(ActionRow.of(m.getList())).queue();
                })
                .setTimeoutTime(5);

        event.getHook().sendMessage("Boop!")
                .setActionRow(buttonManager.getList())
                .queue();

        buttonManager.startTimeout();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String message = event.getAuthor().getAsMention() + " Boop";
        event.getChannel().sendMessage(message)
                .setActionRow(buildButtons(0).setJda(event.getJDA()).getList())
                .queue();
    }
}
