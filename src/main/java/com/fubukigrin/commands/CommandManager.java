package com.fubukigrin.commands;

import java.util.*;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("CommandManager --- ready!");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        // Get name of the command
        String command = event.getName();

        // General commands
        switch (command) {
            case "ping" -> {
                GeneralCommands.ping(event);
            }
            case "boop" -> {
                GeneralCommands.boop(event);
            }
            case "roll" -> {
                GeneralCommands.roll(event);
            }
        }
    }

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        
        //--- General commands ---\\
        commandData.add(Commands.slash("ping", "Ping the bot!"));
        commandData.add(Commands.slash("boop", "Boop!"));
        commandData.add(Commands.slash("roll", "Roll a random number")
                    .addOption(OptionType.INTEGER, "limit", "The highest number of the range, default is 100"));

        //--- Update commands ---\\
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
