package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.scheduler.TaskManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

/**
 * Manager responsible for managing players combat state
 */
public class CombatManager {
    private final Reference2ObjectMap<UUID, User> playersInCombat = new Reference2ObjectOpenHashMap<>();
    private final PluginManager pm = Bukkit.getPluginManager();

    /**
     * Stops all active combat modes
     */
    public void stop() {
        for (var user : playersInCombat.values()) {
            user.deleteBossbar();
            ScoreboardManager.resetScoreboard(user);
            var runnable = user.getRunnable();
            if (runnable != null) runnable.cancel();
        }
        playersInCombat.clear();
    }

    /**
     * Checks if the player with the specified UUID is in combat
     *
     * @param uuid UUID of checkable player
     * @return true if player in combat, false otherwise
     */
    public boolean isInCombat(UUID uuid) {
        return playersInCombat.containsKey(uuid);
    }

    /**
     * Removes specified user from combat map
     *
     * @param user User that will be removed
     */
    public void removeFromCombatMap(User user) {
        this.playersInCombat.remove(user.getPlayerUUID());
    }

    /**
     * Gets User from combat map with the specified player UUID
     *
     * @param uuid player UUID
     * @return User or null if not in combat
     */
    public User getUser(UUID uuid) {
        return playersInCombat.get(uuid);
    }

    /**
     * Initiates combat between two players and calls appropriate combat events.
     *
     * @param damager player giving the damage
     * @param target player receiving the damage
     */
    public void startCombat(Player damager, Player target) {
        if (damager.getName().equals(target.getName())) return;

        var damagerUUID = damager.getUniqueId();
        var targetUUID = target.getUniqueId();

        boolean isDamagerInCombat = isInCombat(damagerUUID);
        boolean isTargetInCombat = isInCombat(targetUUID);

        var damagerUser = this.getOrCreateUser(damagerUUID);
        var targetUser = this.getOrCreateUser(targetUUID);

        boolean alreadyOpponents = damagerUser.containsOpponent(targetUser);

        if (!alreadyOpponents) {
            CombatPreStartEvent preStartEvent = new CombatPreStartEvent(damager, target);
            pm.callEvent(preStartEvent);
            if (preStartEvent.isCancelled()) {
                if (!isDamagerInCombat) removeFromCombatMap(damagerUser);
                if (!isTargetInCombat) removeFromCombatMap(targetUser);
                return;
            }
        }

        CombatDamageEvent event;

        if (alreadyOpponents) {
            event = new CombatContinueEvent(damagerUser, targetUser);
        }
        else if (!isDamagerInCombat && !isTargetInCombat) {
            event = new CombatStartEvent(damagerUser, targetUser);
        }
        else if (isDamagerInCombat && isTargetInCombat) {
            event = new CombatMergeEvent(damagerUser, targetUser);
        } else {
            event = new CombatJoinEvent(
                    damagerUser,
                    targetUser,
                    isTargetInCombat
            );
        }

        pm.callEvent(event);
    }

    /**
     * Starts combat for single player without precombat checks and events calls
     *
     * @param player player to start combat for
     * @apiNote This method don't call any events, use {@link #startCombat(Player, Player)} for 2 players instead
     */
    public void startSingle(Player player) {
        UUID playerUUID = player.getUniqueId();
        User user = getOrCreateUser(playerUUID);
        user.refresh(System.currentTimeMillis());
    }

    /**
     * Gets existing user or creates new one if not found
     *
     * @param uuid player UUID
     * @return existing or new User
     */
    public User getOrCreateUser(UUID uuid) {
        var user = getUser(uuid);
        if (user != null) return user;
        User u = new User(uuid, TaskManager.getEntityScheduler(Bukkit.getPlayer(uuid)));
        playersInCombat.put(uuid, u);
        return u;
    }

    /**
     * Calls CombatEndEvent for specified user
     *
     * @param user User for whom combat ends
     */
    public void stopCombat(User user) {
        pm.callEvent(new CombatEndEvent(user));
    }
}
