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
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import com.fubukigrin.button.ButtonCallback;
import com.fubukigrin.button.ButtonManager;
import com.fubukigrin.button.CustomButton;
import com.fubukigrin.commands.BaseCommand;

@SuppressWarnings("null")
public class TopScores extends BaseCommand {
    // Message id, Scores cache
    private static HashMap<Long, List<Score>> scoreCache = new HashMap<Long, List<Score>>();
    // Message id, index cache
    private static HashMap<Long, Integer> indexCache = new HashMap<Long, Integer>();
    // Message id, userdata cache
    private static HashMap<Long, UserData> userDataCache = new HashMap<Long, UserData>();

    private static String[] altNames = {"top", "t"};
    TopScores() {
        super(
                "topscores",
                "osu",
                "Get top scores of a user",
                altNames);

        addArgs(OptionType.STRING, "user", "The user to get top scores of", false);
    }

    @Override
    public void execute(@Nonnull SlashCommandInteractionEvent event) {
        String user = event.getOption("user").getAsString().replace("\"", "");

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

            MessageEmbed embed = buildEmbed(userData, scores, 0).build();
            ButtonManager buttonManager = buildButtons(0, scores).setJda(event.getJDA());
            
            if (scores.size() / 11 > 0) {
                event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed))
                    .setActionRow(buttonManager.getList())
                    .queue((message) -> {
                        createCache(message.getIdLong(), 0, scores, userData);

                        buttonManager.setTimeoutCallback((ButtonManager m) -> {
                            m.disableAll();
                            event.getHook().editOriginalComponents(ActionRow.of(m.getList())).queue();

                            removeCache(message.getIdLong());
                        })
                        .setTimeoutTime(30);

                        buttonManager.startTimeout();
                    });
            } 
            else {
                // If there are less than 11 scores, only display the first page
                event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(
                    String.format("User `%s` was not found", user));
            MessageEmbed embed = ica.getEmbed().build();

            event.getHook().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
        }
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
            List<Score> scores = osuAPI.getTopScores(uid);
            
            MessageEmbed embed = buildEmbed(userData, scores, 0).build();
            ButtonManager buttonManager = buildButtons(0, scores).setJda(event.getJDA());

            if (scores.size() / 11 > 0) {
                event.getChannel().sendMessage(MessageCreateData.fromEmbeds(embed))
                    .setActionRow(buttonManager.getList())
                    .queue((message) -> {
                        createCache(message.getIdLong(), 0, scores, userData);

                        buttonManager.setTimeoutCallback((ButtonManager m) -> {
                            m.disableAll();
                            message.editMessageComponents(ActionRow.of(m.getList())).queue();

                            removeCache(message.getIdLong());
                        })
                        .setTimeoutTime(30);

                        buttonManager.startTimeout();
                    });
            }
            else {
                // If there are less than 11 scores, only display the first page
                event.getChannel().sendMessage(MessageCreateData.fromEmbeds(embed)).queue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            String error = (user != null) ? String.format("User `%s` was not found", user)
                    : "Invalid arguments! Please try again";

            InvalidCommandArgumentException ica = new InvalidCommandArgumentException(error);
            String errorMessage = ica.getErrorMessage();
            event.getChannel().sendMessage(errorMessage).queue();
        }
    }

    public static EmbedBuilder buildEmbed(UserData userData, List<Score> scores, int index) {
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
        StringBuffer body = new StringBuffer();
        int modulo = (scores.size() % 10 == 0 && scores.size() > 0) ? 10 : scores.size() % 10;

        // Appending scores
        for (int i = 0; i < modulo; i++) {
            Score score = scores.get(index * 10 + i);

            int placement = index * 10 + i + 1;
            String songTitle = score.beatmapset.get("title").asText();
            String mapDifficulty = score.beatmap.get("version").asText();
            String mapTitle = String.format("%s [%s]", songTitle, mapDifficulty);
            String mapLink = score.beatmap.get("url").asText();
            String modCombo = score.getModCombo();
            double starRating = score.beatmap.get("difficulty_rating").asDouble();

            String grade = OsuGrades.valueOf(score.grade).toString();
            double accuracy = score.accuracy * 100.0;
            String miss = (score.missCount > 0) ? String.format("%d%s", score.missCount, Icons.MISS) : "";
            String relativeDate = ConvertDateTime.toRelativeDiscordTimestampInstantFormat(score.timeSet);
            
            body.append(String.format("**#%d** **[%s](%s)** **+%s** [%.2f★]%n", placement, mapTitle, mapLink, modCombo, starRating))
                .append(String.format("%s **%,.2fpp** (%.2f%s) [**%sx**] %s %s%n", 
                        grade, score.pp, accuracy, "%", 
                        score.maxCombo, miss, relativeDate
                ));
        }

        eb.setDescription(body);

        // Footer
        int maxIndex = calculateMaxPageIndex(scores) + 1;
        String footer = String.format("On osu! Bancho | Page %s/%s", index + 1, maxIndex);
        eb.setFooter(footer);

        return eb;
    }

    public static ButtonManager buildButtons(int index, List<Score> scores) {
        ButtonManager buttons = new ButtonManager();

        // Button fullBackwards = Button.secondary("top fullBack", Icons.REWIND);
        // Button backwards = Button.secondary("top backward", Icons.ARROW_BACKWARD);
        // Button fullForwards = Button.secondary("top fullForward",
        // Icons.FAST_FORWARD);
        // Button forwards = Button.secondary("top forward", Icons.ARROW_FORWARD);
        // Button select = Button.secondary("top selectIndex", Icons.ASTERISK);

        CustomButton fullBackwards = new CustomButton("top fullBack", Icons.REWIND, ButtonStyle.SECONDARY);
        fullBackwards.addCallback(new ButtonCallback() {
            @Override
            public void execute(ButtonInteractionEvent event) {
                fullBack(event);
            }
        });

        CustomButton backwards = new CustomButton("top backward", Icons.ARROW_BACKWARD, ButtonStyle.SECONDARY);
        backwards.addCallback(new ButtonCallback() {
            @Override
            public void execute(ButtonInteractionEvent event) {
                backward(event);
            }
        });

        CustomButton fullForwards = new CustomButton("top fullForward", Icons.FAST_FORWARD, ButtonStyle.SECONDARY);
        fullForwards.addCallback(new ButtonCallback() {
            @Override
            public void execute(ButtonInteractionEvent event) {
                fullForward(event);
            }
        });

        CustomButton forwards = new CustomButton("top forward", Icons.ARROW_FORWARD, ButtonStyle.SECONDARY);
        forwards.addCallback(new ButtonCallback() {
            @Override
            public void execute(ButtonInteractionEvent event) {
                forward(event);
            }
        });

        CustomButton select = new CustomButton("top selectIndex", Icons.ASTERISK, ButtonStyle.SECONDARY);
        select.addCallback(new ButtonCallback() {
            @Override
            public void execute(ButtonInteractionEvent event) {
                selectIndex(event);
            }
        });

        // Determine the number of pages a score could have, this code only applies to
        // users who have more than ten scores in their top
        int maxIndex = calculateMaxPageIndex(scores);

        if (index == 0) {
            fullBackwards = fullBackwards.asDisabled();
            backwards = backwards.asDisabled();
        } else if (index == maxIndex) {
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
        indexCache.put(event.getMessageIdLong(), 0);
        updateInteraction(event);
    }

    public static void fullForward(@Nonnull ButtonInteractionEvent event) {
        int maxIndex = calculateMaxPageIndex(scoreCache.get(event.getMessageIdLong()));
        indexCache.put(event.getMessageIdLong(), maxIndex);
        updateInteraction(event);
    }

    public static void backward(@Nonnull ButtonInteractionEvent event) {
        indexCache.put(event.getMessageIdLong(), indexCache.get(event.getMessageIdLong()) - 1);
        updateInteraction(event);
    }

    public static void forward(@Nonnull ButtonInteractionEvent event) {
        indexCache.put(event.getMessageIdLong(), indexCache.get(event.getMessageIdLong()) + 1);
        updateInteraction(event);
    }

    public static void selectIndex(@Nonnull ButtonInteractionEvent event) {
        
    }

    private static void updateInteraction(@Nonnull ButtonInteractionEvent event) {
        long messageId = event.getMessageIdLong();

        MessageEmbed embed = buildEmbed(userDataCache.get(messageId), scoreCache.get(messageId), indexCache.get(messageId)).build();
        ButtonManager buttonManager = buildButtons(indexCache.get(messageId), scoreCache.get(messageId))
                                    .setJda(event.getJDA())
                                    .setTimeoutCallback((ButtonManager m) -> {
                                        m.disableAll();
                                        event.getHook().editOriginalComponents(ActionRow.of(m.getList())).queue();
            
                                        removeCache(messageId);
                                    })
                                    .setTimeoutTime(30);

        event.editMessageEmbeds(embed)
                .setActionRow(buttonManager.getList())
                .queue();
        buttonManager.startTimeout();
    }

    private static void createCache(long id, int index, List<Score> scores, UserData userData) {
        scoreCache.put(id, scores);
        indexCache.put(id, 0);
        userDataCache.put(id, userData);
    }

    private static void removeCache(long id) {
        scoreCache.remove(id);
        indexCache.remove(id);
        userDataCache.remove(id);
    }

    private static int calculateMaxPageIndex(List<Score> scores) {
        int modulo = (scores.size() % 10 == 0) ? 10 : scores.size() % 10;
        return (scores.size() - modulo) / 10;
    }
}
