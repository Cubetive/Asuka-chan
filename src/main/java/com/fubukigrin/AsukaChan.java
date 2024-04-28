package com.fubukigrin;

import java.util.*;

import javax.security.auth.login.LoginException;

import com.fubukigrin.commands.CommandLoader;
import com.fubukigrin.listeners.ButtonListener;
import com.fubukigrin.listeners.EventListener;
import com.fubukigrin.listeners.ModalListener;
import com.fubukigrin.utilities.DotenvConfig;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

@SuppressWarnings("unused")

public class AsukaChan {
    private final ShardManager shardManager;

    public AsukaChan() throws LoginException {
        // Config settings
        DotenvConfig dotenvConfig = new DotenvConfig();
        Dotenv config = dotenvConfig.getConfig();
        String token = config.get("TOKEN");

        // Initializing the builder
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        // Enable intents
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        // Setting the bot's status
        builder.setStatus(OnlineStatus.ONLINE);
        // Setting the bot's activity
        builder.setActivity(Activity.watching("fubukiGrin"));
        // Building the bot with ShardManager
        shardManager = builder.build();
        // Load commands
        CommandLoader.load();

        // Register listeners
        shardManager.addEventListener(new EventListener(),
                new CommandManager(),
                new ButtonListener(),
                new ModalListener());
    }

    public static void main(String[] args) {
        try {
            AsukaChan asukaChan = new AsukaChan();
            asukaChan.onReady();
        } catch (LoginException le) {
            System.out.println("Error: Unable to login! Reason: " + le);
        }
    }

    public void onReady() {
        System.out.println("System: Bot is online!");
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
