package dev.enco.greatcombat.core.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.events.*;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.config.ConfigManager;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

/**
 * Manager responsible for managing players combat state
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CombatManager implements ICombatManager {
    private final Reference2ObjectMap<UUID, IUser> playersInCombat = new Reference2ObjectOpenHashMap<>();
    private final PluginManager pm = Bukkit.getPluginManager();
    private final IScoreboardManager scoreboardManager;
    private final ITaskManager taskManager;
    private final ConfigManager configManager;

    /**
     * Stops all active combat modes
     */
    @Override
    public void stop() {
        for (var user : playersInCombat.values()) {
            user.deleteBossbar();
            scoreboardManager.resetScoreboard(user);
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
    @Override
    public boolean isInCombat(UUID uuid) {
        return playersInCombat.containsKey(uuid);
    }

    /**
     * Removes specified user from combat map
     *
     * @param user User that will be removed
     */
    @Override
    public void removeFromCombatMap(IUser user) {
        this.playersInCombat.remove(user.getPlayerUUID());
    }

    /**
     * Gets User from combat map with the specified player UUID
     *
     * @param uuid player UUID
     * @return User or null if not in combat
     */
    @Override
    public IUser getUser(UUID uuid) {
        return playersInCombat.get(uuid);
    }

    /**
     * Initiates combat between two players and calls appropriate combat events.
     *
     * @param damager player giving the damage
     * @param target player receiving the damage
     */
    @Override
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
    @Override
    public void startSingle(Player player) {
        UUID playerUUID = player.getUniqueId();
        IUser user = getOrCreateUser(playerUUID);
        user.refresh(System.currentTimeMillis());
    }

    /**
     * Gets existing user or creates new one if not found
     *
     * @param uuid player UUID
     * @return existing or new User
     */
    @Override
    public IUser getOrCreateUser(UUID uuid) {
        var user = getUser(uuid);
        if (user != null) return user;
        User u = new User(
                uuid,
                this,
                taskManager.getEntityScheduler(Bukkit.getPlayer(uuid)),
                configManager,
                scoreboardManager
        );
        playersInCombat.put(uuid, u);
        return u;
    }

    /**
     * Calls CombatEndEvent for specified user
     *
     * @param user User for whom combat ends
     */
    @Override
    public void stopCombat(IUser user) {
        pm.callEvent(new CombatEndEvent(user));
    }
}
