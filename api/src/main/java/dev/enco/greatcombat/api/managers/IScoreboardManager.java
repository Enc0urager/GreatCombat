package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;

public interface IScoreboardManager extends IManager {
    /**
     * Sets the scoreboard provider implementation directly.
     *
     * @param scoreboardProvider The scoreboard provider instance to use
     */
    void setProvider(ScoreboardProvider scoreboardProvider);

    /**
     * Sets the combat scoreboard for a user with the specified time display.
     * Only sets the scoreboard if scoreboards are enabled in the configuration.
     *
     * @param user User to set the scoreboard for
     * @param time The formatted time string to display on the scoreboard
     */
    void setScoreboard(IUser user, String time);

    /**
     * Resets the scoreboard for a user when combat ends.
     * Only resets the scoreboard if scoreboards are enabled in the configuration.
     *
     * @param user User to reset the scoreboard for
     */
    void resetScoreboard(IUser user);
}
