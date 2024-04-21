package com.fubukigrin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.annotation.Nonnull;

import java.io.*;
import java.net.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fubukigrin.utilities.DotenvConfig;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandManager extends ListenerAdapter {
    // Get the Class of the command, where <name_id, command_class>
    private static HashMap<String, String> commandClass = new HashMap<String, String>();
    // Get the category of the command, where <name_id, category>
    private static HashMap<String, String> commandCategory = new HashMap<String, String>();
    // Get the main command name from altername command names, where <alt_name, name_id>
    private static HashMap<String, String> commandAlt = new HashMap<String, String>();
    
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
            content = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/main/java/com/fubukigrin/commands/config.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<CommandData> commandData = new ArrayList<>();
        // extract json to read command details
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<JsonNode> cmds = objectMapper.readValue(content, new TypeReference<List<JsonNode>>() {});
            for (JsonNode cmd: cmds) {
                SlashCommandData sc = Commands.slash(cmd.get("name_id").asText(), cmd.get("description").asText());
                JsonNode ar = cmd.get("args");
                for (JsonNode arg: ar) {
                    sc.addOption(OptionType.valueOf(arg.get("type").asText()), arg.get("name").asText(), arg.get("description").asText());
                }
                commandData.add(sc);
                commandClass.put(cmd.get("name_id").asText(), cmd.get("command_class").asText());
                commandCategory.put(cmd.get("name_id").asText(), cmd.get("category").asText());

                // Add alternate command names
                JsonNode alts = cmd.get("alt_names");
                for (JsonNode alt: alts) {
                    commandAlt.put(alt.asText(), cmd.get("name_id").asText());
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            e.printStackTrace();
        }
    }

    // Prefix command listener
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        DotenvConfig dotenvConfig = new DotenvConfig();
        Dotenv config = dotenvConfig.getConfig();
        // Check if the command structure is valid
        String prefix = config.get("PREFIX");
        String[] message = event.getMessage().getContentRaw().split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        // If message does not contain prefix or the author isn't the user, immediately returns
        if (!message[0].startsWith(prefix) || event.getAuthor().isBot() || event.getAuthor().isSystem()) return;

        // Get name of the command and its arguments
        String command = message[0].substring(prefix.length()).toLowerCase();
        String[] args = (message.length >= 2) ? Arrays.copyOfRange(message, 1, message.length) : null;
        // If the command name is an alt, get the main command name
        if (commandAlt.containsKey(command)) command = commandAlt.get(command);

        try {
            Class c = cl.loadClass("com.fubukigrin.commands." + commandCategory.get(command) + "." + commandClass.get(command));
            Method m = c.getMethod("execute", new Class[] {MessageReceivedEvent.class, String[].class});
            m.invoke(null, event, args);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "null", "rawtypes", "unchecked" })
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String[] components = event.getButton().getId().split(" ");
        String classComponent = components[0];
        String methodComponent = components[1];
        
        try {
            Class c = cl.loadClass("com.fubukigrin.commands." + commandCategory.get(classComponent) + "." + commandClass.get(classComponent));
            Method m = c.getMethod(methodComponent, new Class[] {ButtonInteractionEvent.class});
            m.invoke(null, event);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
