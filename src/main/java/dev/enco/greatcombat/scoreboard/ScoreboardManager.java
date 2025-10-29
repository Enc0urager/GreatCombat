package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.config.settings.Scoreboard;
import dev.enco.greatcombat.manager.User;
import dev.enco.greatcombat.scoreboard.impl.FastBoardProvider;
import dev.enco.greatcombat.scoreboard.impl.TABProvider;
import dev.enco.greatcombat.utils.Placeholders;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing scoreboard operations during combat.
 * Provides functionality to set, update, and reset combat scoreboards for players.
 *
 * @see ScoreboardProvider
 */
@UtilityClass
public class ScoreboardManager {
    private ScoreboardProvider provider;

    /**
     * Sets the scoreboard provider implementation based on the specified provider name.
     *
     * @param s The name of the scoreboard provider to use
     */
    public void setProvider(String s) {
        Locale locale = ConfigManager.getLocale();
        try {
            ScoreboardProviderType type = ScoreboardProviderType.valueOf(s.toUpperCase());
            setProvider(type);
            Logger.info(locale.sbProvider().replace("{0}", s));
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.sbError(), s));
        }
    }

    /**
     * Sets the scoreboard provider implementation directly.
     *
     * @param scoreboardProvider The scoreboard provider instance to use
     */
    public void setProvider(ScoreboardProvider scoreboardProvider) {
        provider = scoreboardProvider;
    }

    /**
     * Sets the scoreboard provider implementation based on the provider type.
     *
     * @param type The type of scoreboard provider to use
     */
    public void setProvider(ScoreboardProviderType type) {
        setProvider(type.getProvider());
    }

    /**
     * Sets the combat scoreboard for a user with the specified time display.
     * Only sets the scoreboard if scoreboards are enabled in the configuration.
     *
     * @param user User to set the scoreboard for
     * @param time The formatted time string to display on the scoreboard
     */
    public void setScoreboard(User user, String time) {
        Scoreboard boardSettings = ConfigManager.getScoreboard();
        if (boardSettings.enable()) {
            provider.setScoreboard(user, boardSettings.title(), getLines(user, time));
        }
    }

    /**
     * Resets the scoreboard for a user when combat ends.
     * Only resets the scoreboard if scoreboards are enabled in the configuration.
     *
     * @param user User to reset the scoreboard for
     */
    public void resetScoreboard(User user) {
        Scoreboard boardSettings = ConfigManager.getScoreboard();
        if (boardSettings.enable()) {
            provider.resetScoreboard(user);
        }
    }

    /**
     * Generates the formatted lines for the scoreboard based on the user's current state.
     * Processes placeholders and handles opponent list expansion.
     *
     * @param user The User object to generate scoreboard lines for
     * @param time The formatted time string to include in the lines
     * @return List of formatted scoreboard lines with placeholders replaced
     */
    private List<String> getLines(User user, String time) {
        Scoreboard boardSettings = ConfigManager.getScoreboard();
        List<String> replaced = new ArrayList<>();
        for (var line : boardSettings.lines()) {
            if (line.contains("{opponents}")) replaced.addAll(getOpponents(user));
            else {
                replaced.add(Placeholders.replace(user.toPlayer(), line.replace("{time}", time)));
            }
        }
        return replaced;
    }

    /**
     * Generates the formatted opponent list for the scoreboard.
     * Returns a specific message if the user has no opponents.
     *
     * @param user User to get opponents for
     * @return List of formatted opponent entries or empty state message
     */
    private List<String> getOpponents(User user) {
        Scoreboard boardSettings = ConfigManager.getScoreboard();
        List<String> opponentsList = new ArrayList<>();
        var opponents = user.getOpponents();
        if (opponents.isEmpty()) opponentsList.add(boardSettings.empty());
        for (var opponent : opponents) {
            opponentsList.add(Placeholders.replaceInBoard(opponent.toPlayer(), boardSettings.opponent()));
        }
        return opponentsList;
    }
}
