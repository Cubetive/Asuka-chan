package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import com.fubukigrin.commands.BaseCommand;
import com.fubukigrin.components.button.ButtonManager;
import com.fubukigrin.components.button.CustomButton;

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
                        .addCallback((ButtonInteractionEvent event, ButtonManager manager) -> {
                            String message = event.getUser().getAsMention() + " Boop!";
                            event.reply(message).queue();
                        }))
                .add(new CustomButton("disable_all", "Disable all", ButtonStyle.DANGER)
                        .addCallback((ButtonInteractionEvent event, ButtonManager manager) -> {
                            manager.disableAll(event.getHook());
                        }))
                .add(new CustomButton("remove_all", "Remove all", ButtonStyle.DANGER)
                        .addCallback((ButtonInteractionEvent event, ButtonManager manager) -> {
                            manager.removeAll(event.getHook());
                        }));
        return buttons;
    }

    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        ButtonManager buttonManager = buildButtons(0)
                .setJda(event.getJDA())
                .setTimeoutCallback((ButtonManager m) -> {
                    m.disableAll(event.getHook());
                })
                .setTimeoutTime(5);

        event.getHook().sendMessage("Boop!")
                .setActionRow(buttonManager.getList())
                .queue();

        buttonManager.startTimeout();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String content = event.getAuthor().getAsMention() + " Boop";
        ButtonManager buttonManager = buildButtons(0).setJda(event.getJDA());

        event.getChannel().sendMessage(content)
                .setActionRow(buttonManager.getList())
                .queue((message) -> {
                    buttonManager.setTimeoutCallback((ButtonManager m) -> {
                        m.disableAll(message);
                    })
                            .setTimeoutTime(5);

                    buttonManager.startTimeout();
                });
    }
}
