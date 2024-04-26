package com.fubukigrin.commands.osu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.fubukigrin.commands.InvalidCommandArgumentException;
import com.fubukigrin.utilities.ColorTheme;
import com.fubukigrin.utilities.ConvertDateTime;
import com.fubukigrin.utilities.OsuGrades;
import com.osu.OsuAPI;
import com.osu.UserData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import com.fubukigrin.commands.BaseCommand;

public class Osu extends BaseCommand {
    private static String ONLINE = "https://cubetive.s-ul.eu/kLWl06Lw";
    private static String OFFLINE = "https://cubetive.s-ul.eu/pEQn8tB0";

    Osu() {
        super(
                "osu",
                "osu",
                "Get information about an osu! user",
                "profile");

        addArgs(OptionType.STRING, "user", "The user to get information about", true);
    }

    @SuppressWarnings("null")
    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        String user = (event.getOption("user") != null) ? event.getOption("user").getAsString().replace("\"", "") : " ";
        MessageEmbed embed;

        try {
            OsuAPI osuAPI = new OsuAPI();
            UserData userData = osuAPI.getUser(user);

            embed = buildEmbed(userData).build();
        } catch (Exception e) {
            e.printStackTrace();
            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(
                    String.format("User `%s` was not found", user));
            embed = ica.getEmbed().build();
        }

        event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
    }

    public void execute(@Nonnull MessageReceivedEvent event, String[] args) {
        String user = null;

        try {
            if (args.length != 1)
                throw new Exception();

            user = args[0].replace("\"", "");
            OsuAPI osuAPI = new OsuAPI();
            UserData userData = osuAPI.getUser(user);

            MessageEmbed embed = buildEmbed(userData).build();
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

    public static EmbedBuilder buildEmbed(UserData userData) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(ColorTheme.DEFAULT);

        // Title
        String title = String.format("%s: %,.2fpp (#%,d %s%,d)", userData.username, userData.pp, userData.globalRank,
                userData.countryCode, userData.countryRank);
        String profileLink = "https://osu.ppy.sh/users/" + userData.id + "/osu";
        String iconURL = "https://assets.ppy.sh/old-flags/" + userData.countryCode + ".png";
        eb.setAuthor(title, profileLink, iconURL);

        // Avatar and flag
        eb.setThumbnail(userData.avatarUrl.toString());

        // Body
        int playTimeHours = userData.playTime / 3600;
        double currentLevel = userData.levelCurrent + (userData.levelProgress / 100.0);

        List<Integer> grades = new ArrayList<Integer>();
        userData.gradeCounts.forEach((grade) -> {
            grades.add(grade.asInt());
        });
        Collections.swap(grades, 0, 1); // Swap XH and X
        Collections.swap(grades, 2, 3); // Swap SH and S

        String rankBuilder = String.format("**▸ Ranks:** %s`pl`%s`pl`%s`pl`%s`pl`%s`pl`%n", OsuGrades.XH, OsuGrades.X,
                OsuGrades.SH, OsuGrades.S, OsuGrades.A);
        rankBuilder = rankBuilder.replace("pl", "%,d"); // Replacing placeholder with integer format

        StringBuffer body = new StringBuffer();
        body.append(String.format("**▸ Peak rank:** `#%,d` achieved %s%n", userData.peakRank,
                ConvertDateTime.toRelativeDiscordTimestamp(userData.peakRankUpdate)))
                .append(String.format("**▸ Accuracy:** `%,.2f%s` • **Level:** `%s`%n", userData.accuracy, "%",
                        currentLevel))
                .append(String.format("**▸ Playcount:** `%,d` (`%d hrs`)%n", userData.playCount, playTimeHours))
                .append(String.format("**▸ Medals:** `%d` • **Badges:** `%d`%n", userData.achievements.size(),
                        userData.badges.size()))
                .append(String.format(rankBuilder, grades.toArray()))
                .append(String.format("**▸ Join date:** `%s` (%s)", ConvertDateTime.toShortDate(userData.joinDate),
                        ConvertDateTime.toRelativeDiscordTimestamp(userData.joinDate)));

        eb.setDescription(body.toString());

        // Footer (nothing right now)
        String footer = (userData.isOnline || userData.lastSeen == "null")
                ? "On osu! Bancho"
                : String.format("Last seen %s on osu! Bancho", ConvertDateTime.toRelativeTime(userData.lastSeen));
        String status = (userData.isOnline) ? ONLINE : OFFLINE;

        eb.setFooter(footer, status);

        return eb;
    }
}
