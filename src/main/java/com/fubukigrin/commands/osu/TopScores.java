package com.fubukigrin.commands.osu;

import java.util.*;
import javax.annotation.Nonnull;

import com.fubukigrin.commands.InvalidCommandArgumentException;
import com.fubukigrin.utilities.ColorTheme;
import com.fubukigrin.utilities.ConvertDateTime;
import com.fubukigrin.utilities.Icons;
import com.fubukigrin.utilities.OsuGrades;
import com.osu.OsuAPI;
import com.osu.Score;
import com.osu.UserData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@SuppressWarnings("null")
public class TopScores {
    // Message id, Scores cache
    private HashMap<String, List<Score>> scoreCache = new HashMap<String, List<Score>>();
    // Message id, index cache
    private HashMap<String, Integer> indexCache = new HashMap<String, Integer>();

    public static void execute(@Nonnull SlashCommandInteractionEvent event) {
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
            List<Score> scores = osuAPI.getTopScores(uid);
            
            embed = buildEmbed(userData, scores).build();
        } 
        catch (Exception e) {
            e.printStackTrace();
            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(String.format("User `%s` was not found", user));
            embed = ica.getEmbed().build();
        }

        event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed))
                       .setActionRow(buildButtons(0))
                       .queue();
    }

    public static void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String user = null;

        try {
            if (args.length != 1) throw new Exception();
            
            user = args[0].replace("\"", "");
            OsuAPI osuAPI = new OsuAPI();
            int uid = 0;
            if (user != null)
                uid = osuAPI.getUser(user).id;
            else {
                // do sth with the database i guess
            }
            
            UserData userData = osuAPI.getUser(user);
            List<Score> scores = osuAPI.getTopScores(uid);
            
            MessageEmbed embed = buildEmbed(userData, scores).build();
            event.getChannel().sendMessage(MessageCreateData.fromEmbeds(embed))
                              .setActionRow(buildButtons(0))
                              .queue();
        } 
        catch (Exception e) {
            e.printStackTrace();
            String error = (user != null) ? String.format("User `%s` was not found", user) : "Invalid arguments! Please try again";

            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(error);
            String errorMessage = ica.getErrorMessage();
            event.getChannel().sendMessage(errorMessage).queue();
        }
    }

    public static EmbedBuilder buildEmbed(UserData userData, List<Score> scores) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(ColorTheme.DEFAULT);

        // Title
        String title = String.format("%s: %,.2fpp (#%,d %s%,d)", userData.username, userData.pp, userData.globalRank, userData.countryCode, userData.countryRank);
        String profileLink = "https://osu.ppy.sh/users/" + userData.id + "/osu";
        String iconURL = "https://assets.ppy.sh/old-flags/" + userData.countryCode + ".png";
        eb.setAuthor(title, profileLink, iconURL);

        // Avatar and flag
        eb.setThumbnail(userData.avatarUrl.toString());

        // Body


        return eb;
    }

    public static List<Button> buildButtons(int index) {
        List<Button> buttons = new ArrayList<Button>();

        Button fullBackwards = Button.secondary("top fullBack", Icons.REWIND);
        Button backwards = Button.secondary("top backward", Icons.ARROW_BACKWARD);
        Button fullForwards = Button.secondary("top fullForward", Icons.FAST_FORWARD);
        Button forwards = Button.secondary("top forward", Icons.ARROW_FORWARD);
        Button select = Button.secondary("top selectIndex", Icons.ASTERISK);

        if (index == 0) {
            fullBackwards = fullBackwards.asDisabled();
            backwards = backwards.asDisabled();
        }
        else if (index == 9) {
            fullForwards = fullForwards.asDisabled();
            forwards = forwards.asDisabled();
        }

        buttons.add(fullBackwards);
        buttons.add(backwards);
        buttons.add(select);
        buttons.add(forwards);
        buttons.add(fullForwards);

        return buttons;
    }

    public static void fullBack(@Nonnull ButtonInteractionEvent event) {

    }

    public static void fullForward(@Nonnull ButtonInteractionEvent event) {
        System.out.println("full forward");
    }

    public static void backward(@Nonnull ButtonInteractionEvent event) {
        System.out.println("backward");
    }

    public static void forward(@Nonnull ButtonInteractionEvent event) {
        System.out.println("forward");
    }

    public static void selectIndex(@Nonnull ButtonInteractionEvent event) {
        System.out.println("select index");
    }
}
