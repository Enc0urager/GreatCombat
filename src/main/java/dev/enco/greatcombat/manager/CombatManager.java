package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.scheduler.TaskManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manager responsible for managing players combat state
 */
public class CombatManager {
    private final Set<User> playersInCombat = new HashSet<>();
    private final PluginManager pm = Bukkit.getPluginManager();

    /**
     * Stops all active combat modes
     */
    public void stop() {
        for (var user : playersInCombat) {
            user.deleteBossbar();
            ScoreboardManager.resetScoreboard(user);
            var runnable = user.getRunnable();
            if (runnable != null) runnable.cancel();
        }
    }

    /**
     * Checks if the player with the specified UUID is in combat
     *
     * @param uuid UUID of checkable player
     * @return true if player in combat, false otherwise
     */
    public boolean isInCombat(UUID uuid) {
        return playersInCombat.stream()
                .anyMatch(user -> user.getPlayerUUID().equals(uuid));
    }

    /**
     * Removes specified user from combat map
     *
     * @param user User that will be removed
     */
    public void removeFromCombatMap(User user) {
        this.playersInCombat.remove(user);
    }

    /**
     * Gets User from combat map with the specified player UUID
     *
     * @param uuid player UUID
     * @return User or null if not in combat
     */
    public User getUser(UUID uuid) {
        return playersInCombat.stream()
                .filter(user -> user.getPlayerUUID().equals(uuid))
                .findFirst()
                .orElse(null);
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
            if (preStartEvent.isCancelled()) return;
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
     * Gets existing user or creates new one if not found
     *
     * @param uuid player UUID
     * @return existing or new User
     */
    public User getOrCreateUser(UUID uuid) {
        var user = getUser(uuid);
        if (user != null) return user;
        User u = new User(uuid, TaskManager.getEntityScheduler(Bukkit.getPlayer(uuid)));
        playersInCombat.add(u);
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
