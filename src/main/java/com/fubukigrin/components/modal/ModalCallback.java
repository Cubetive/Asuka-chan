package com.fubukigrin.components.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalCallback {
    void execute(ModalInteractionEvent event);
}
