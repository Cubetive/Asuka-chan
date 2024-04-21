package com.fubukigrin.commands.osu;

import com.fubukigrin.CommandManager;

public class InitOsu {

    public static void load() {
        // Register commands
        System.out.println("Osu commands --- loading");

        Osu osu = new Osu();
        CommandManager.registerCommand(osu);

        RecentScores recentScores = new RecentScores();
        CommandManager.registerCommand(recentScores);

        TopScores topScores = new TopScores();
        CommandManager.registerCommand(topScores);

        System.out.println("Osu commands --- loaded!");
    }
}
