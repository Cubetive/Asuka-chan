package com.fubukigrin.button;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.fubukigrin.listeners.ButtonListener;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

public class CustomButton extends ButtonImpl {

    private long Timeout;
    private final ArrayList<ButtonCallback.Click> ButtonCallbackList = new ArrayList<ButtonCallback.Click>();

    public CustomButton(String id, String label, ButtonStyle style) {
        this(id, label, style, null, false, null, -1);
    }

    public CustomButton(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
        this(id, label, style, null, disabled, emoji, -1);
    }

    public CustomButton(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji, long timeout) {
        this(id, label, style, null, disabled, emoji, timeout);
    }

    public CustomButton(String id, String label, ButtonStyle style, String url, boolean disabled, Emoji emoji,
            long timeout) {
        super(id + "_" + System.currentTimeMillis(), label, style, url, disabled, emoji);
        ButtonListener.registerButton(getId(), this);
        Timeout = timeout;
    }

    public CustomButton addCallback(ButtonCallback.Click callback) {
        ButtonCallbackList.add(callback);
        return this;
    }

    public void execute(ButtonInteractionEvent event) {
        for (ButtonCallback.Click callback : ButtonCallbackList) {
            callback.execute(event);
            System.out.println(getId() + " executed");
        }
    }

    @Override
    @Nonnull
    public CustomButton asDisabled() {
        return new CustomButton(getId(), getLabel(), getStyle(), true, getEmoji());
    }
}
