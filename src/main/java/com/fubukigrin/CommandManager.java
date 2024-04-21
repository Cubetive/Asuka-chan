package com.fubukigrin;

import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.Nonnull;

import com.fubukigrin.commands.BaseCommand;
import com.fubukigrin.commands.CommandLoader;
import com.fubukigrin.utilities.DotenvConfig;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

public class CommandManager extends ListenerAdapter {
    private static DotenvConfig dotenvConfig = new DotenvConfig();
    private static Dotenv config = dotenvConfig.getConfig();
    private static String prefix = config.get("PREFIX");

    // Get the Class of the command, where <name_id, command_class>
    private static HashMap<String, BaseCommand> commandClass = new HashMap<String, BaseCommand>();
    // Get the category of the command, where <name_id, category>
    private static HashMap<String, BaseCommand> commandCategory = new HashMap<String, BaseCommand>();
    // Get the main command name from altername command names, where <alt_name,
    // name_id>
    private static HashMap<String, BaseCommand> commandAlt = new HashMap<String, BaseCommand>();

    public static void registerCommand(@Nonnull BaseCommand command) {
        if (commandClass.containsKey(command.NameId)) {
            return;
        }

        commandClass.put(command.NameId, command);
        commandCategory.put(command.CommandCategory, command);
        for (String altName : command.AltNames) {
            commandAlt.put(altName, command);
        }
    }

    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("CommandManager --- ready!");
        CommandLoader.main();
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        System.out.println("Adding commands --- ready!");

        CommandCreateAction cmd;
        for (BaseCommand command : commandClass.values()) {
            cmd = event.getJDA().upsertCommand(command.NameId, command.Description);
            if (command.Args != null) {
                cmd.addOptions(command.Args);
            }
            cmd.queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        // Get name id of the command
        String command = event.getName();

        try {
            BaseCommand c = commandClass.get(command);
            c.execute(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        // Check if the command structure is valid
        String[] message = event.getMessage().getContentRaw().split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        // If message does not contain prefix or the author isn't the user, immediately
        // returns
        if (!message[0].startsWith(prefix) || event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        // Get name of the command and its arguments
        String command_name = message[0].substring(prefix.length()).toLowerCase();
        BaseCommand command;
        // If the command name is an alt, get the main command name
        if (commandAlt.containsKey(command_name))
            command = commandAlt.get(command_name);
        else
            command = commandClass.get(command_name);
        String[] args = (message.length >= 2) ? Arrays.copyOfRange(message, 1, message.length) : null;

        try {
            command.execute(event, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
