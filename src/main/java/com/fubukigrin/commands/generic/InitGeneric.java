package com.fubukigrin.commands.generic;

import com.fubukigrin.CommandManager;

public class InitGeneric {

    public static void load() {
        // Register commands
        System.out.println("Generic commands --- loading");

        Boop boop = new Boop();
        CommandManager.registerCommand(boop);

        Ping ping = new Ping();
        CommandManager.registerCommand(ping);

        Roll roll = new Roll();
        CommandManager.registerCommand(roll);

        System.out.println("Generic commands --- loaded!");
    }
}
