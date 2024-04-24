package com.fubukigrin.button;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

import net.dv8tion.jda.api.JDA;

public class ButtonManager {

    private final ArrayList<CustomButton> buttonList = new ArrayList<CustomButton>();

    private JDA jdaContext;
    private TimeoutCallback timeoutCallback;
    private long timeoutTime;
    private ScheduledFuture<?> timeoutFuture;

    public interface TimeoutCallback {
        void execute(ButtonManager buttonManager);
    }

    public ButtonManager() {
        this(null, null, -1);
    }

    public ButtonManager(JDA jdaContext) {
        this(jdaContext, null, -1);
    }

    public ButtonManager(JDA jdaContext, TimeoutCallback timeoutCallback, long timeout) {
        super();

        this.jdaContext = jdaContext;
        this.timeoutCallback = timeoutCallback;
        this.timeoutTime = timeout;
    }

    public void startTimeout() {
        if (timeoutFuture != null) {
            timeoutFuture.cancel(true);
        }

        if (timeoutCallback != null && timeoutTime > 0) {
            timeoutFuture = jdaContext.getRateLimitPool().schedule(() -> timeoutCallback.execute(this), timeoutTime,
                    TimeUnit.SECONDS);
        }
    }

    public ButtonManager setJda(JDA jdaContext) {
        this.jdaContext = jdaContext;
        return this;
    }

    public ButtonManager setTimeoutCallback(@Nonnull TimeoutCallback timeoutCallback) {
        this.timeoutCallback = timeoutCallback;
        return this;
    }

    public ButtonManager setTimeoutTime(long timeoutTime) {
        this.timeoutTime = timeoutTime;
        return this;
    }

    public ButtonManager add(CustomButton button) {
        button.setManager(this);
        buttonList.add(button);
        return this;
    }

    public ArrayList<CustomButton> getList() {
        return buttonList;
    }

    public void update(ArrayList<CustomButton> buttonList) {
        this.buttonList.clear();
        this.buttonList.addAll(buttonList);
    }

    public CustomButton get(int index) {
        return buttonList.get(index);
    }

    public void disableAll() {
        ArrayList<CustomButton> buttonListUpdate = new ArrayList<CustomButton>();
        for (CustomButton button : buttonList) {
            buttonListUpdate.add(button.asDisabled());
        }

        update(buttonListUpdate);
    }
}
