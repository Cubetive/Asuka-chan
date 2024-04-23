package com.fubukigrin.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonCallback {
    public interface Click {
        void execute(ButtonInteractionEvent event);
    }

    public interface Timeout {
        void execute();
    }
}
