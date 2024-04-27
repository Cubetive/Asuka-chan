package com.fubukigrin.components.modal;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.internal.interactions.component.ModalImpl;

public class CustomModal extends ModalImpl {

    private final ArrayList<ModalCallback> modalCallbackList = new ArrayList<ModalCallback>();

    public CustomModal(String id, String title, List<ActionRow> components) {
        super(id + "_" + System.currentTimeMillis(), title, components);
    }

    public CustomModal addCallback(ModalCallback callback) {
        modalCallbackList.add(callback);
        return this;
    }

    public void execute(ModalInteractionEvent event) {
        for (ModalCallback callback : modalCallbackList) {
            callback.execute(event);
        }
    }

}
