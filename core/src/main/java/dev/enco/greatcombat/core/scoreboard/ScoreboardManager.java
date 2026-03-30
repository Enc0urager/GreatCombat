package dev.enco.greatcombat.core.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.config.settings.Scoreboard;
import dev.enco.greatcombat.core.utils.Placeholders;
import dev.enco.greatcombat.core.utils.logger.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing scoreboard operations during combat.
 * Provides functionality to set, update, and reset combat scoreboards for players.
 *
 * @see ScoreboardProvider
 */
@Singleton
public class ScoreboardManager implements IScoreboardManager {
    private final ConfigManager configManager;
    private ScoreboardProvider provider;

    @Inject
    public ScoreboardManager(ConfigManager configManager) {
        this.configManager = configManager;
        setProvider(configManager.getMainConfig().getString("scoreboard-manager"));
    }

    /**
     * Sets the scoreboard provider implementation based on the specified provider name.
     *
     * @param s The name of the scoreboard provider to use
     */
    public void setProvider(String s) {
        Locale locale = configManager.getLocale();
        try {
            ScoreboardProviderType type = ScoreboardProviderType.valueOf(s.toUpperCase());
            setProvider(type);
            Logger.info(locale.sbProvider().replace("{0}", s));
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.sbError(), s));
            setProvider(ScoreboardProviderType.FASTBOARD);
        }
    }

    /**
     * Sets the scoreboard provider implementation directly.
     *
     * @param scoreboardProvider The scoreboard provider instance to use
     */
    @Override
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
    @Override
    public void setScoreboard(IUser user, String time) {
        Scoreboard boardSettings = configManager.getScoreboard();
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
    @Override
    public void resetScoreboard(IUser user) {
        Scoreboard boardSettings = configManager.getScoreboard();
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
    private List<String> getLines(IUser user, String time) {
        Scoreboard boardSettings = configManager.getScoreboard();
        List<String> replaced = new ArrayList<>();
        for (var line : boardSettings.lines()) {
            if (line.contains("{opponents}")) replaced.addAll(getOpponents(user));
            else {
                replaced.add(Placeholders.replace(user.asPlayer(), line.replace("{time}", time)));
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
    private List<String> getOpponents(IUser user) {
        Scoreboard boardSettings = configManager.getScoreboard();
        List<String> opponentsList = new ArrayList<>();
        var opponents = user.getOpponents();
        if (opponents.isEmpty()) opponentsList.add(boardSettings.empty());
        for (var opponent : opponents) {
            opponentsList.add(Placeholders.replaceInBoard(opponent.asPlayer(), boardSettings.opponent()));
        }
        return opponentsList;
    }
}
