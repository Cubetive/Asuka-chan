package com.fubukigrin.listeners;

import java.util.HashMap;

import javax.annotation.Nonnull;

import com.fubukigrin.components.modal.CustomModal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {

    private static HashMap<String, CustomModal> modalMapping = new HashMap<String, CustomModal>();

    public static void registerModal(CustomModal modal) {
        modalMapping.put(modal.getId(), modal);
    }

    public static void unregisterModal(String id) {
        modalMapping.remove(id);
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        event.deferReply().queue();
        String modalId = event.getModalId();
        if (modalMapping.containsKey(modalId)) {
            try {
                modalMapping.get(modalId).execute(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
