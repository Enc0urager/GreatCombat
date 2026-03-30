package dev.enco.greatcombat.core.manager;

import dev.enco.greatcombat.api.events.CombatTickEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Bossbar;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entity for managing combat state, opponents, timer, and visual effects.
 */
@Data
public class User implements IUser {
    private ObjectSet<IUser> opponents = new ObjectOpenHashSet<>();
    private long startPvpTime;
    private final UUID playerUUID;
    private BossBar bossBar;
    private final ICombatManager combatManager;
    private WrappedTask<?> runnable;
    private static final PluginManager pm = Bukkit.getPluginManager();
    private final IScheduler scheduler;
    private final ConfigManager configManager;
    private final IScoreboardManager scoreboardManager;

    public String getOpponentsFormatted(String delimiter) {
        return opponents.stream()
                .map(IUser::asPlayer).map(Player::getName)
                .collect(Collectors.joining(delimiter));

    }

    /**
     * Converts the user's UUID to a Player
     *
     * @return the Player associated with this user's UUID
     */
    @Override
    public Player asPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    /**
     * Removes this user from all opponents' opponent lists
     */
    @Override
    public void removeFromOpponentsMaps() {
        for (var user : opponents) user.removeOpponent(this);
    }

    /**
     * Starts the combat timer for this user
     * Creates bossbar and schedules periodic combat tick events
     */
    @Override
    public void startTimer() {
        createBossbar();
        Settings settings = configManager.getSettings();
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
    @Override
    public void refresh(long start) {
        this.startPvpTime = start;
        if (this.runnable != null) runnable.cancel();
        deleteBossbar();
        startTimer();
    }

    /**
     * Removes and cleans up the bossbar for this user
     */
    @Override
    public void deleteBossbar() {
        if (bossBar != null) {
            bossBar.removePlayer(asPlayer());
            this.bossBar = null;
        }
    }

    /**
     * Adds an opponent to this user's opponent list
     *
     * @param opponent the user to add as an opponent
     */
    @Override
    public void addOpponent(IUser opponent) {
        this.opponents.add(opponent);
    }

    /**
     * Removes an opponent from this user's opponent list
     *
     * @param opponent the user to remove from opponents
     */
    @Override
    public void removeOpponent(IUser opponent) {
        this.opponents.remove(opponent);
    }

    /**
     * Creates a bossbar for this user if bossbars are enabled in configuration
     */
    @Override
    public void createBossbar() {
        Bossbar barSettings = configManager.getBossbar();
        Settings settings = configManager.getSettings();
        String time = Time.format(settings.combatTime());
        scoreboardManager.setScoreboard(this, time);
        if (bossBar == null && barSettings.enable()) {
            bossBar = Bukkit.createBossBar(
                    barSettings.title().replace("{time}", time),
                    barSettings.color(),
                    barSettings.style()
            );
            bossBar.setProgress(1.0);
            bossBar.addPlayer(asPlayer());
            bossBar.setVisible(true);
        }
    }

    /**
     * Checks if the specified user is in this user's opponent list
     *
     * @param user the user to check
     * @return true if the user is an opponent, false otherwise
     */
    @Override
    public boolean containsOpponent(IUser user) {
        return this.opponents.contains(user);
    }

    /**
     * Calculates the remaining combat time in milliseconds
     *
     * @return the remaining time until combat ends in milliseconds
     */
    @Override
    public long getRemaining() {
        long leftTime = System.currentTimeMillis() - startPvpTime;
        Settings settings = configManager.getSettings();
        long totalTime = settings.combatTime() * 1000L;
        return totalTime - leftTime;
    }

    /**
     * Updates the scoreboard and bossbar with current remaining time
     *
     * @param remainingTime the remaining combat time in milliseconds
     */
    @Override
    public void updateBoardAndBar(long remainingTime) {
        String time = Time.format((int) remainingTime / 1000);
        scoreboardManager.setScoreboard(this, time);
        if (this.bossBar != null) {
            Bossbar barSettings = configManager.getBossbar();
            Settings settings = configManager.getSettings();
            bossBar.setTitle(barSettings.title().replace("{time}", time));
            if (barSettings.progress()) {
                double progress = (double) remainingTime / (settings.combatTime() * 1000);
                bossBar.setProgress(progress);
            }
        }
    }

    /**
     * Compares this User to the specified object.
     *
     * @return true if object equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IUser user)) return false;
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
