package com.fubukigrin;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class AsukaChan {
    private static final String token = "MTIyODc5ODYwNTg4NDc4NDczMg.GydoZV.9Df6bwrPLY3AnoaW3bWaacgNHbdB4wT6bgjxrU";

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);
    
        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("fubukiGrin"));
        
        builder.build();
    }
}
