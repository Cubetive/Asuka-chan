package com.fubukigrin.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonCallback {
    void execute(ButtonInteractionEvent event);
}
