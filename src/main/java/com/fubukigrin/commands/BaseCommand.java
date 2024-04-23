package com.fubukigrin.commands;

import java.util.ArrayList;

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
    public String Description;
    public ArrayList<OptionData> Args;
    public String[] AltNames;

    public void SetNameId(String name_id) {
        NameId = name_id;
    }

    public void SetCommandClass(String command_class) {
        CommandClass = command_class;
    }

    public void SetDescription(String description) {
        Description = description;
    }

    public void AddArgs(OptionData arg) {
        if (Args == null) {
            Args = new ArrayList<OptionData>();
        }
        Args.add(arg);
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
            String description,
            ArrayList<OptionData> args,
            String[] alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        Description = description;
        Args = args;
        AltNames = alt_names;
    }

    public BaseCommand(
            String name_id,
            String command_class,
            String description,
            OptionData[] args,
            String alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        Description = description;
        Args = new ArrayList<OptionData>();
        AltNames = new String[] { alt_names };
    }

    public BaseCommand(
            String name_id,
            String command_class,
            String description,
            String alt_names) {
        NameId = name_id;
        CommandClass = command_class;
        Description = description;
        Args = null;
        AltNames = new String[] { alt_names };
    }

}