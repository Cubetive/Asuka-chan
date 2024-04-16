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
    // Get the Class of the command, where <name_id, command_class>
    private static Hashtable<String, String> commandClass = new Hashtable<String, String>();
    // Get the category of the command, where <name_id, category>
    private static Hashtable<String, String> commandCategory = new Hashtable<String, String>();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("CommandManager --- ready!");
    }

    // variables for command calling
    private static File file;
    private static URL url;
    private static URL[] urls;
    private static ClassLoader cl;

    // Path directory (me when different people uses different operating system, real)
    private static final String WINDOWS_10 = "\\src\\main\\java\\com\\fubukigrin\\commands\\config.json";
    private static final String MAC_OS_X = "/src/main/java/com/fubukigrin/commands/config.json";

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        // Clear the command category on guild ready
        commandCategory.clear();

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
            String path = "";
            switch (System.getProperty("os.name")) {
                case "Windows 10" -> {
                    path = WINDOWS_10;
                }
                case "Mac OS X" -> {
                    path = MAC_OS_X;
                }
            }

            content = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<CommandData> commandData = new ArrayList<>();
        // extract json to read command details
        JSONArray cmds = new JSONArray(content);
        for (int i = 0; i < cmds.length(); i++) {
            JSONObject cmd = cmds.getJSONObject(i);
            SlashCommandData sc = Commands.slash(cmd.getString("name_id"), cmd.getString("description"));

            if (cmd.has("args")) {
                JSONArray args = cmd.getJSONArray("args");
                for (int j = 0; j < args.length(); j++) {
                    JSONObject arg = args.getJSONObject(j);
                    sc.addOption(OptionType.valueOf(arg.getString("type")), arg.getString("name"), arg.getString("description"));
                }
            }

            commandData.add(sc);
            commandClass.put(cmd.getString("name_id"), cmd.getString("command_class"));
            commandCategory.put(cmd.getString("name_id"), cmd.getString("category"));
        }

        //--- Update commands ---\\
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        // Get name id of the command
        String command = event.getName();

        try {
            Class c = cl.loadClass("com.fubukigrin.commands." + commandCategory.get(command) + "." + commandClass.get(command));
            Method m = c.getMethod("execute", new Class[] {SlashCommandInteractionEvent.class});
            m.invoke(null, event);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
