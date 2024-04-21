package com.fubukigrin.commands;

import java.awt.Color;

import com.fubukigrin.utilities.ColorTheme;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * This exception class is mainly for the bot to return 
 * funny messages and stuff along the line lol. Maybe it can 
 * be implemented later? :3c
 */
public class InvalidCommandArgumentException {
    private String errorMessage;
    private Color embedColor = ColorTheme.ERROR;

    public InvalidCommandArgumentException(String message) {
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public EmbedBuilder getEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(embedColor);
        eb.setDescription(errorMessage);

        return eb;
    }
}
