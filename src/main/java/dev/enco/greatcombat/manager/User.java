package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.api.CombatTickEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Bossbar;
import dev.enco.greatcombat.config.settings.Scoreboard;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.scheduler.IScheduler;
import dev.enco.greatcombat.scheduler.WrappedTask;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.utils.Time;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity for managing combat state, opponents, timer, and visual effects.
 */
@Data
public class User {
    private List<User> opponents = new ArrayList<>();
    private long startPvpTime;
    private final UUID playerUUID;
    private BossBar bossBar;
    private static final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();
    private WrappedTask<?> runnable;
    private static final PluginManager pm = Bukkit.getPluginManager();
    private final IScheduler scheduler;

    /**
     * Converts the user's UUID to a Player
     *
     * @return the Player associated with this user's UUID
     */
    public Player toPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    /**
     * Removes this user from all opponents' opponent lists
     */
    public void removeFromOpponentsMaps() {
        for (var user : opponents) user.removeOpponent(this);
    }

    /**
     * Starts the combat timer for this user
     * Creates bossbar and schedules periodic combat tick events
     */
    public void startTimer() {
        createBossbar();
        Settings settings = ConfigManager.getSettings();
        this.runnable = scheduler.runRepeating(() ->
            pm.callEvent(new CombatTickEvent(User.this)),
                20L,
                    settings.tickInterval()
        );
    }

    /**
     * Refreshes the combat timer with new start time
     * Cancels existing timer, removes bossbar, and restarts timer
     *
     * @param start the new start time in milliseconds
     */
    public void refresh(long start) {
        this.startPvpTime = start;
        if (this.runnable != null) runnable.cancel();
        deleteBossbar();
        startTimer();
    }

    /**
     * Removes and cleans up the bossbar for this user
     */
    public void deleteBossbar() {
        if (bossBar != null) {
            bossBar.removePlayer(Bukkit.getPlayer(getPlayerUUID()));
            this.bossBar = null;
        }
    }

    /**
     * Adds an opponent to this user's opponent list
     *
     * @param opponent the user to add as an opponent
     */
    public void addOpponent(User opponent) {
        this.opponents.add(opponent);
    }

    /**
     * Removes an opponent from this user's opponent list
     *
     * @param opponent the user to remove from opponents
     */
    public void removeOpponent(User opponent) {
        this.opponents.remove(opponent);
    }

    /**
     * Creates a bossbar for this user if bossbars are enabled in configuration
     */
    public void createBossbar() {
        Bossbar barSettings = ConfigManager.getBossbar();
        Settings settings = ConfigManager.getSettings();
        String time = Time.format(settings.combatTime());
        ScoreboardManager.setScoreboard(this, time);
        if (bossBar == null && barSettings.enable()) {
            bossBar = Bukkit.createBossBar(
                    barSettings.title().replace("{time}", time),
                    barSettings.color(),
                    barSettings.style()
            );
            bossBar.setProgress(1.0);
            bossBar.addPlayer(Bukkit.getPlayer(playerUUID));
            bossBar.setVisible(true);
        }
    }

    /**
     * Checks if the specified user is in this user's opponent list
     *
     * @param user the user to check
     * @return true if the user is an opponent, false otherwise
     */
    public boolean containsOpponent(User user) {
        return this.opponents.contains(user);
    }

    /**
     * Calculates the remaining combat time in milliseconds
     *
     * @return the remaining time until combat ends in milliseconds
     */
    public long getRemaining() {
        long leftTime = System.currentTimeMillis() - startPvpTime;
        Settings settings = ConfigManager.getSettings();
        long totalTime = settings.combatTime() * 1000;
        return totalTime - leftTime;
    }

    /**
     * Updates the scoreboard and bossbar with current remaining time
     *
     * @param remainingTime the remaining combat time in milliseconds
     */
    public void updateBoardAndBar(long remainingTime) {
        String time = Time.format((int) remainingTime / 1000);
        ScoreboardManager.setScoreboard(this, time);
        if (this.bossBar != null) {
            Bossbar barSettings = ConfigManager.getBossbar();
            Settings settings = ConfigManager.getSettings();
            bossBar.setTitle(barSettings.title().replace("{time}", time));
            if (barSettings.progress()) {
                double progress = (double) remainingTime / (settings.combatTime() * 1000);
                bossBar.setProgress(progress);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof User user)) return false;
        return user.getPlayerUUID().equals(this.getPlayerUUID());
    }

    /**
     * Generates hash code based on player's UUID
     *
     * @return hash code of the player's UUID
     */
    @Override
    public int hashCode() {
        return playerUUID.hashCode();
    }
}
