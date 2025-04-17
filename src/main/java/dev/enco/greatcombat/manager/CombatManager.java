package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.CombatEndEvent;
import dev.enco.greatcombat.api.CombatStartEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Powerups;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class CombatManager {
    private final Set<User> playersInCombat = new HashSet<>();
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Powerups powerups = ConfigManager.getPowerups();
    private final Settings settings = ConfigManager.getSettings();
    private final Messages messages = ConfigManager.getMessages();

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
        if (damager.equals(target)) return;
        if (PowerupsManager.hasPowerups(damager, powerups.preventableDamagerPowerups())
                || PowerupsManager.hasPowerups(target, powerups.preventableTargetPowerups())) return;

        var damagerUUID = damager.getUniqueId();
        var targetUUID = target.getUniqueId();

        var damagerUser  = this.getOrCreateUser(damagerUUID);
        var targetUser  = this.getOrCreateUser(targetUUID);

        if (!damagerUser.containsOpponent(targetUser))
            damagerUser.addOpponent(targetUser);
        if (!targetUser.containsOpponent(damagerUser))
            targetUser.addOpponent(damagerUser);

        boolean isDamagerInCombat = isInCombat(damagerUUID);
        boolean isTargetInCombat = isInCombat(targetUUID);

        long start = System.currentTimeMillis();

        if (isDamagerInCombat) damagerUser.refresh(start);
        else {
            if (!damager.hasPermission("greatcombat.powerups.bypass"))
                PowerupsManager.disablePowerups(damager, powerups.disablingDamagerPowerups());
            ActionExecutor.execute(damager, messages.onStartDamager(), target.getName(), "");
            damagerUser.setStartPvpTime(start);
            damagerUser.startTimer();
            this.playersInCombat.add(damagerUser);
        }

        if (isTargetInCombat) targetUser.refresh(start);
        else {
            if (!target.hasPermission("greatcombat.powerups.bypass"))
                PowerupsManager.disablePowerups(target, powerups.disablingTargetPowerups());
            ActionExecutor.execute(target, messages.onStartDamager(), damager.getName(), "");
            targetUser.setStartPvpTime(start);
            targetUser.startTimer();
            this.playersInCombat.add(targetUser);
        }

        if (!isDamagerInCombat && !isTargetInCombat) {
            pm.callEvent(new CombatStartEvent(damagerUser, targetUser, e));
        }
    }

    public User getOrCreateUser(UUID playerUUID) {
        var user = getUser(playerUUID);
        if (user == null) return new User(playerUUID);
        return user;
    }

    public void stopCombat(User user) {
        pm.callEvent(new CombatEndEvent(user));
    }
}
