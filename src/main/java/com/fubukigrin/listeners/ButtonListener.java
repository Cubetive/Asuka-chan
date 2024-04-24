package com.fubukigrin.listeners;

import java.util.HashMap;

import javax.annotation.Nonnull;

import com.fubukigrin.button.CustomButton;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonListener extends ListenerAdapter {
    private static HashMap<String, CustomButton> buttonMapping = new HashMap<String, CustomButton>();

    public static void registerButton(String id, CustomButton button) {
        buttonMapping.put(id, button);
    }

    public static void unregisterButton(String id) {
        buttonMapping.remove(id);
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String compId = event.getComponentId();
        if (buttonMapping.containsKey(compId)) {
            try {
                buttonMapping.get(compId).execute(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
