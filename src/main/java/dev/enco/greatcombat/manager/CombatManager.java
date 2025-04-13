package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.api.CombatEndEvent;
import dev.enco.greatcombat.api.CombatJoinEvent;
import dev.enco.greatcombat.api.CombatStartEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Powerups;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.powerups.PowerupsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.PluginManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CombatManager {
    private final List<User> playersInCombat = new ArrayList<>();
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Powerups powerups = ConfigManager.getPowerups();
    private final Settings settings = ConfigManager.getSettings();

    public void stop() {
        for (var user : playersInCombat) {
            user.deleteBossbar();
            ScoreboardManager.resetScoreboard(user);
            var runnable = user.getRunnable();
            if (runnable != null) runnable.cancel();
        }
    }

    public boolean isInCombat(UUID uuid) {
        return playersInCombat.stream()
                .anyMatch(user -> user.getPlayerUUID().equals(uuid));
    }

    public void removeFromCombatMap(User user) {
        this.playersInCombat.remove(user);
    }

    public User getUser(UUID uuid) {
        return playersInCombat.stream()
                .filter(user -> user.getPlayerUUID().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void startCombat(Player damager, Player target, Cancellable e) {
        if (settings.ignoredWorlds().contains(damager.getWorld().getName())) return;

        var damagerUUID = damager.getUniqueId();
        var targetUUID = target.getUniqueId();

        var damagerUser  = this.getOrCreateUser(damagerUUID);
        var targetUser  = this.getOrCreateUser(targetUUID);

        if (PowerupsManager.hasPowerups(damager, powerups.preventableDamagerPowerups())
                || PowerupsManager.hasPowerups(target, powerups.preventableTargetPowerups())) return;

        long start = System.currentTimeMillis();

        boolean isDamagerInCombat = isInCombat(damagerUUID);
        boolean isTargetInCombat = isInCombat(targetUUID);

        if (isDamagerInCombat && isTargetInCombat) {
            damagerUser.refresh(start);
            targetUser.refresh(start);
            damagerUser.setStartPvpTime(start);
            targetUser.setStartPvpTime(start);
            return;
        }

        if (!damagerUser.containsOpponent(targetUser)) {
            damagerUser.addOpponent(targetUser);
        }

        if (!targetUser.containsOpponent(damagerUser)) {
            targetUser.addOpponent(damagerUser);
        }

        this.playersInCombat.add(damagerUser);
        this.playersInCombat.add(targetUser);

        damagerUser.setStartPvpTime(start);
        targetUser.setStartPvpTime(start);

        damagerUser.startTimer();
        targetUser.startTimer();

        if (!isDamagerInCombat && !isTargetInCombat) {
            pm.callEvent(new CombatStartEvent(damagerUser, targetUser, e));
        } else {
            pm.callEvent(new CombatJoinEvent(damagerUser, targetUser, e));
        }
    }

    private User getOrCreateUser(UUID playerUUID) {
        if (!isInCombat(playerUUID)) {
            var newUser = new User(playerUUID);
            return newUser;
        }
        return getUser(playerUUID);
    }

    public void stopCombat(User user) {
        pm.callEvent(new CombatEndEvent(user));
    }
}
