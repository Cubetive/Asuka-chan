package com.fubukigrin;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class AsukaChan {
    private final Dotenv config;
    private final ShardManager shardManager;

    public AsukaChan() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("fubukiGrin"));
        shardManager = builder.build();
    }

    public static void main(String[] args) {
        try {
            AsukaChan asukaChan = new AsukaChan();
        } catch (LoginException le) {
            System.out.println("Error: Unable to login! Reason: " + le);
        }
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }
}
