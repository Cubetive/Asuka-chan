package com.fubukigrin.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

interface IBaseCommand {

    void execute(@Nonnull SlashCommandInteractionEvent event);

    void execute(@Nonnull MessageReceivedEvent event,
            String[] args);

}

public abstract class BaseCommand implements IBaseCommand {
    public String NameId;
    public String CommandClass;
    public String CommandCategory;
    public String Description;
    public OptionData[] Args;
    public String[] AltNames;

    public BaseCommand(
            String name_id,
            String command_class,
            String category,
            String description,
            OptionData[] args,
            String[] alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        CommandCategory = category;
        Description = description;
        Args = args;
        AltNames = alt_names;
    }

    public BaseCommand(
            String name_id,
            String command_class,
            String category,
            String description,
            OptionData[] args,
            String alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        CommandCategory = category;
        Description = description;
        Args = new OptionData[] {};
        AltNames = new String[] { alt_names };
    }

    public BaseCommand(
            String name_id,
            String command_class,
            String category,
            String description,
            String alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        CommandCategory = category;
        Description = description;
        Args = null;
        AltNames = new String[] { alt_names };
    }

}