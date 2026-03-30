package dev.enco.greatcombat.api.models;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public interface IUser {
    /**
     * Converts the user's UUID to a Player
     *
     * @return the Player associated with this user's UUID
     */
    Player asPlayer();
    UUID getPlayerUUID();
    /**
     * Removes this user from all opponents' opponent lists
     */
    void removeFromOpponentsMaps();
    /**
     * Starts the combat timer for this user
     * Creates bossbar and schedules periodic combat tick events
     */
    void startTimer();
    /**
     * Refreshes the combat timer with new start time
     * Cancels existing timer, removes bossbar, and restarts timer
     *
     * @param start the new start time in milliseconds
     */
    void refresh(long start);
    /**
     * Removes and cleans up the bossbar for this user
     */
    void deleteBossbar();
    /**
     * Adds an opponent to this user's opponent list
     *
     * @param opponent the user to add as an opponent
     */
    void addOpponent(IUser opponent);
    /**
     * Removes an opponent from this user's opponent list
     *
     * @param opponent the user to remove from opponents
     */
    void removeOpponent(IUser opponent);
    /**
     * Creates a bossbar for this user if bossbars are enabled in configuration
     */
    void createBossbar();
    /**
     * Checks if the specified user is in this user's opponent list
     *
     * @param user the user to check
     * @return true if the user is an opponent, false otherwise
     */
    boolean containsOpponent(IUser user);
    /**
     * Calculates the remaining combat time in milliseconds
     *
     * @return the remaining time until combat ends in milliseconds
     */
    long getRemaining();
    /**
     * Updates the scoreboard and bossbar with current remaining time
     *
     * @param remainingTime the remaining combat time in milliseconds
     */
    void updateBoardAndBar(long remainingTime);
    Set<IUser> getOpponents();
    WrappedTask<?> getRunnable();
    void setStartPvpTime(long l);
    String getOpponentsFormatted(String delimiter);
}
