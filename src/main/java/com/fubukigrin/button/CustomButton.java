package com.fubukigrin.button;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.fubukigrin.listeners.ButtonListener;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

public class CustomButton extends ButtonImpl {

    private final ArrayList<ButtonCallback> ButtonCallbackList = new ArrayList<ButtonCallback>();
    private ButtonManager managerCtx;

    public void setManager(ButtonManager manager) {
        this.managerCtx = manager;
    }

    public CustomButton(String id, String label, ButtonStyle style) {
        this(id, label, style, null, false, null);
    }

    public CustomButton(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
        this(id, label, style, null, disabled, emoji);
    }

    public CustomButton(String id, String label, ButtonStyle style, String url, boolean disabled, Emoji emoji) {
        super(id + "_" + System.currentTimeMillis(), label, style, url, disabled, emoji);
        ButtonListener.registerButton(getId(), this);
    }

    public CustomButton(CustomButton button, boolean disabled, ButtonManager manager) {
        super(button.getId(), button.getLabel(), button.getStyle(), button.getUrl(), disabled, button.getEmoji());
        this.managerCtx = manager;

        for (ButtonCallback callback : button.ButtonCallbackList) {
            this.addCallback(callback);
        }
    }

    public CustomButton addCallback(ButtonCallback callback) {
        ButtonCallbackList.add(callback);
        return this;
    }

    public void execute(ButtonInteractionEvent event) {
        for (ButtonCallback callback : ButtonCallbackList) {
            callback.execute(event);
            System.out.println(getId() + " executed");
        }

        if (managerCtx != null) {
            // restart timeout when button is executed
            managerCtx.startTimeout();
        }
    }

    @Override
    @Nonnull
    public CustomButton asDisabled() {
        return new CustomButton(this, true, managerCtx);
    }
}
