package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ICombatManager extends IManager {
    /**
     * Stops all active combat modes
     */
    void stop();

    /**
     * Checks if the player with the specified UUID is in combat
     *
     * @param uuid UUID of checkable player
     * @return true if player in combat, false otherwise
     */
    boolean isInCombat(@NotNull UUID uuid);

    /**
     * Removes specified user from combat map
     *
     * @param user User that will be removed
     */
    void removeFromCombatMap(@NotNull IUser user);

    /**
     * Gets User from combat map with the specified player UUID
     *
     * @param uuid player UUID
     * @return User or null if not in combat
     */
    @Nullable IUser getUser(@NotNull UUID uuid);

    /**
     * Initiates combat between two players and calls appropriate combat events.
     *
     * @param damager player giving the damage
     * @param target  player receiving the damage
     */
    void startCombat(@NotNull Player damager,
                     @NotNull Player target);

    /**
     * Starts combat for single player without precombat checks and events calls
     *
     * @param player player to start combat for
     * @apiNote This method don't call any events, use {@link #startCombat(Player, Player)} for 2 players instead
     */
    void startSingle(@NotNull Player player);

    /**
     * Gets existing user or creates new one if not found
     *
     * @param uuid player UUID
     * @return existing or new User
     */
    @NotNull IUser getOrCreateUser(@NotNull UUID uuid);

    /**
     * Calls CombatEndEvent for specified user
     *
     * @param user User for whom combat ends
     */
    void stopCombat(@NotNull IUser user);
}
