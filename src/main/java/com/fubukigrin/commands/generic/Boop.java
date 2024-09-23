package com.fubukigrin.commands.generic;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import com.fubukigrin.commands.BaseCommand;
import com.fubukigrin.components.button.ButtonManager;
import com.fubukigrin.components.button.CustomButton;
import com.fubukigrin.components.modal.CustomModal;

public class Boop extends BaseCommand {

    Boop() {
        super(
                "boop",
                "generic",
                "Boop!",
                "");
    }

    public static CustomModal buildModal() {
        TextInput subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
                .setPlaceholder("Subject of this ticket")
                .setMinLength(10)
                .setMaxLength(100) // or setRequiredRange(10, 100)
                .build();

        TextInput body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Your concerns go here")
                .setMinLength(30)
                .setMaxLength(1000)
                .build();

        Modal modal = Modal.create("modmail", "Modmail")
                .addActionRows(ActionRow.of(subject), ActionRow.of(body))
                .build();

        return CustomModal.copyFromModal(modal);
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
                            event.deferEdit().complete();
                            manager.disableAll(event.getHook());
                        }))
                .add(new CustomButton("remove_all", "Remove all", ButtonStyle.DANGER)
                        .addCallback((ButtonInteractionEvent event, ButtonManager manager) -> {
                            event.deferEdit().complete();
                            manager.removeAll(event.getHook());
                        }))
                .add(new CustomButton("modal_callback", "Modal", ButtonStyle.SECONDARY)
                        .addCallback((ButtonInteractionEvent event, ButtonManager manager) -> {
                            event.replyModal(buildModal()).queue();
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
