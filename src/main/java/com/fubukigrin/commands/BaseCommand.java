package com.fubukigrin.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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

    public void SetNameId(String name_id) {
        NameId = name_id;
    }

    public void SetCommandClass(String command_class) {
        CommandClass = command_class;
    }

    public void SetCommandCategory(String category) {
        CommandCategory = category;
    }

    public void SetDescription(String description) {
        Description = description;
    }

    public void AddArgs(OptionData arg) {
        if (Args == null) {
            Args = new OptionData[] { arg };
        } else {
            OptionData[] newArgs = new OptionData[Args.length + 1];
            System.arraycopy(Args, 0, newArgs, 0, Args.length);
            newArgs[Args.length] = arg;
            Args = newArgs;
        }
    }

    public void AddArgs(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description,
            boolean isRequired) {
        AddArgs(new OptionData(type, name, description, isRequired));
    }

    public BaseCommand() {
    }

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