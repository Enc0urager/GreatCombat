package dev.enco.greatcombat.api.models;

import java.util.List;

/**
 * Interface defining the contract for scoreboard provider implementations.
 */
public interface ScoreboardProvider {
    /**
     * Sets the scoreboard for a user with the specified title and lines.
     *
     * @param user User to set the scoreboard for
     * @param title The title to display at the top of the scoreboard
     * @param lines The list of lines to display on the scoreboard
     */
    void setScoreboard(IUser user, String title, List<String> lines);
    /**
     * Resets the scoreboard for a user
     *
     * @param user User oto reset the scoreboard for
     */
    void resetScoreboard(IUser user);
}
