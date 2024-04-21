package com.fubukigrin.commands.osu;

import java.util.*;
import javax.annotation.Nonnull;

import com.fubukigrin.commands.InvalidCommandArgumentException;
import com.fubukigrin.utilities.ConvertDateTime;
import com.fubukigrin.utilities.OsuGrades;
import com.osu.OsuAPI;
import com.osu.Score;
import com.osu.UserData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import com.fubukigrin.commands.BaseCommand;

@SuppressWarnings("null")
public class RecentScores extends BaseCommand {

    RecentScores() {
        super(
                "recentscores",
                "osu",
                "Get recent scores of a user",
                "user",
                new OptionData[] {
                        new OptionData(
                                OptionType.STRING, "user", "User to get recent scores from", true)
                },
                "");
    }

    @Override
    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        String user = event.getOption("user").getAsString().replace("\"", "");
        MessageEmbed embed;

        try {
            OsuAPI osuAPI = new OsuAPI();
            int uid = 0;
            if (user != "")
                uid = osuAPI.getUser(user).id;
            else {
                // do sth with the database i guess
            }

            UserData userData = osuAPI.getUser(user);
            List<Score> scores = osuAPI.getRecentScores(uid);

            embed = buildEmbed(userData, scores).build();
        } catch (Exception e) {
            e.printStackTrace();
            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(
                    String.format("User `%s` was not found", user));
            embed = ica.getEmbed().build();
        }

        event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
    }

    @Override
    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String user = null;

        try {
            if (args.length != 1)
                throw new Exception();

            user = args[0].replace("\"", "");
            OsuAPI osuAPI = new OsuAPI();
            int uid = 0;
            if (user != null)
                uid = osuAPI.getUser(user).id;
            else {
                // do sth with the database i guess
            }

            UserData userData = osuAPI.getUser(user);
            List<Score> scores = osuAPI.getRecentScores(uid);

            MessageEmbed embed = buildEmbed(userData, scores).build();
            event.getChannel().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
        } catch (Exception e) {
            e.printStackTrace();
            String error = (user != null) ? String.format("User `%s` was not found", user)
                    : "Invalid arguments! Please try again";

            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(error);
            String errorMessage = ica.getErrorMessage();
            event.getChannel().sendMessage(errorMessage).queue();
        }
    }

    public static EmbedBuilder buildEmbed(UserData userData, List<Score> scores) {
        return null;
    }
}
