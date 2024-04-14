package com.fubukigrin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.net.*;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("CommandManager --- ready!");
    }

    // variables for command calling
    private static File file;
    private static URL url;
    private static URL[] urls;
    private static ClassLoader cl;

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        //// fetch java command classes
        file = new File("commands\\");
        try {
            url = file.toURI().toURL();
            urls = new URL[]{url};
            cl = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        //// add slash commands
        String content; // read json file
        try {
            content = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\main\\java\\com\\fubukigrin\\commands\\config.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<CommandData> commandData = new ArrayList<>();
        // extract json to read command details
        JSONArray cmds = new JSONArray(content);
        for (int i = 0; i < cmds.length(); i++) {
            JSONObject cmd = cmds.getJSONObject(i);
            SlashCommandData sc = Commands.slash(cmd.getString("name"), cmd.getString("description"));

            if (cmd.has("args")) {
                JSONArray args = cmd.getJSONArray("args");
                for (int j = 0; j < args.length(); j++) {
                    JSONObject arg = args.getJSONObject(j);
                    sc.addOption(OptionType.valueOf(arg.getString("type")), arg.getString("name"), arg.getString("description"));
                }
            }

            commandData.add(sc);
        }

        //--- Update commands ---\\
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        // Get name of the command
        String command = event.getName();

        try {
            Class c = cl.loadClass("com.fubukigrin.commands." + command);
            Method m = c.getMethod("execute", new Class[] {SlashCommandInteractionEvent.class});
            m.invoke(null, event);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

/*
// testing code
public static void main(String[] args) {
    try {
        String content = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\main\\java\\com\\fubukigrin\\commands\\config.json")));
        JSONArray cmds = new JSONArray(content);
        for (int i = 0; i < cmds.length(); i++) {
            JSONObject cmd = cmds.getJSONObject(i);
            System.out.println(cmd.getString("name"));
            System.out.println(cmd);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    File file = new File("commands\\");
    try {
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);

        Class c = cl.loadClass("com.fubukigrin.commands.ping");
        Method m = c.getMethod("execute", new Class[] {SlashCommandInteractionEvent.class});
        m.invoke(null);
    } catch (MalformedURLException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
        System.out.println("???");
        // throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
    }
}
 */