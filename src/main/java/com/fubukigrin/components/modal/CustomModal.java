package com.fubukigrin.components.modal;

import java.util.ArrayList;
import java.util.List;

import com.fubukigrin.listeners.ModalListener;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.internal.interactions.component.ModalImpl;

public class CustomModal extends ModalImpl {

    private final ArrayList<ModalCallback> modalCallbackList = new ArrayList<ModalCallback>();
    private final Object lock = new Object();

    private boolean executed = false;
    private ModalInteractionEvent event;

    public static CustomModal copyFromModal(Modal modal) {
        return new CustomModal(modal.getId(), modal.getTitle(), modal.getActionRows());
    }

    public CustomModal(String id, String title, List<ActionRow> components) {
        super(id + "_" + System.currentTimeMillis(), title, components);
        ModalListener.registerModal(this);
    }

    public CustomModal addCallback(ModalCallback callback) {
        modalCallbackList.add(callback);
        return this;
    }

    public void execute(ModalInteractionEvent event) {
        for (ModalCallback callback : modalCallbackList) {
            callback.execute(event);
        }

        System.out.println("System: Modal executed!");

        this.event = event;
        this.executed = true;
        // notify all waiting threads
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * Blocks the current Thread and wait for modal to be interacted with.
     * Used for synchronous logic.
     */
    public ModalInteractionEvent waitForExecution() {
        try {
            synchronized (lock) { // use the lock object for synchronization
                while (!executed) {
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return event;
    }

}
