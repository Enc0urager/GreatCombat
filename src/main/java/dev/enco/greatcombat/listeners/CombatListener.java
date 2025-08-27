package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Powerups;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.manager.User;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.utils.Time;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CombatListener implements Listener {
    private final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onPreStart(CombatPreStartEvent e) {
        Player damager = e.getDamager();
        Player target = e.getTarget();

        Settings settings = ConfigManager.getSettings();
        if (settings.ignoredWorlds().contains(damager.getWorld().getName())) {
            e.setCancelled(true); return; }

        Powerups powerups = ConfigManager.getPowerups();
        if (PowerupsManager.hasPowerups(damager, powerups.preventableDamagerPowerups())
                || PowerupsManager.hasPowerups(target, powerups.preventableTargetPowerups()))
            e.setCancelled(true);
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onContinue(CombatContinueEvent e) {
        long now = System.currentTimeMillis();
        e.getTarget().refresh(now);
        e.getDamager().refresh(now);
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onJoin(CombatJoinEvent e) {
        User user = e.isDamagerJoiner() ? e.getTarget() : e.getDamager();
        User joiner = e.isDamagerJoiner() ? e.getDamager() : e.getTarget();
        addBoth(joiner, user);
        Player userPlayer = user.toPlayer();
        long now = System.currentTimeMillis();
        user.refresh(now);
        Messages messages = ConfigManager.getMessages();
        ActionExecutor.execute(joiner.toPlayer(), messages.onJoin(), userPlayer.getName());
        Powerups powerups = ConfigManager.getPowerups();
        processStart(joiner, powerups, now, e.getDamager().getPlayerUUID().equals(joiner.getPlayerUUID()));
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onStart(CombatStartEvent e) {
        User damager = e.getDamager();
        User target = e.getTarget();
        addBoth(damager, target);
        long now = System.currentTimeMillis();

        Powerups powerups = ConfigManager.getPowerups();
        processStart(damager, powerups, now, true);
        processStart(target, powerups, now, false);

        Messages messages = ConfigManager.getMessages();
        Player damagerPlayer = damager.toPlayer();
        Player targetPlayer = target.toPlayer();
        ActionExecutor.execute(damagerPlayer, messages.onStartDamager(), targetPlayer.getName());
        ActionExecutor.execute(targetPlayer, messages.onStartTarget(), damagerPlayer.getName());
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onMerge(CombatMergeEvent e) {
        User damager = e.getDamager();
        User target = e.getTarget();
        addBoth(damager, target);
        long now = System.currentTimeMillis();
        damager.refresh(now);
        target.refresh(now);
        Player damagerPlayer = damager.toPlayer();
        Player targetPlayer = target.toPlayer();
        Messages messages = ConfigManager.getMessages();
        ActionExecutor.execute(damagerPlayer, messages.onMerge(), targetPlayer.getName());
        ActionExecutor.execute(targetPlayer, messages.onMerge(), damagerPlayer.getName());
    }

    private void processStart(User user, Powerups powerups, long now, boolean damager) {
        Player player = user.toPlayer();
        PowerupsManager.disablePowerups(player, damager ? powerups.disablingDamagerPowerups() : powerups.disablingTargetPowerups());
        user.setStartPvpTime(now);
        user.startTimer();
    }

    private void addBoth(User user1, User user2) {
        user1.addOpponent(user2);
        user2.addOpponent(user1);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onEndCombat(CombatEndEvent e) {
        var user = e.getUser();
        var runnable = user.getRunnable();
        if (runnable != null) runnable.cancel();
        user.removeFromOpponentsMaps();
        user.deleteBossbar();
        ScoreboardManager.resetScoreboard(user);
        combatManager.removeFromCombatMap(user);
        var player = user.toPlayer();
        Messages messages = ConfigManager.getMessages();
        ActionExecutor.execute(player, messages.onStop());
        CooldownManager.clearPlayerCooldowns(player);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatQuit(PlayerLeaveInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        var player = user.toPlayer();
        Messages messages = ConfigManager.getMessages();
        Settings settings = ConfigManager.getSettings();
        if (settings.killOnLeave() && !player.hasPermission("greatcombat.kill.bypass")) {
            player.setHealth(0);
            ActionExecutor.execute(player, messages.onPvpLeave(), player.getName());
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatKick(PlayerKickInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        var player = user.toPlayer();
        Messages messages = ConfigManager.getMessages();
        Settings settings = ConfigManager.getSettings();
        if (settings.killOnKick() && !player.hasPermission("greatcombat.kill.bypass")) {
            var kickmessages = settings.kickMessages();
            if (kickmessages.isEmpty() || kickmessages.contains(e.getReason())) {
                player.setHealth(0);
                ActionExecutor.execute(player, messages.onPvpLeave(), player.getName());
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatTick(CombatTickEvent e) {
        var user = e.getUser();
        long remainingTime = user.getRemaining();
        Messages messages = ConfigManager.getMessages();
        Settings settings = ConfigManager.getSettings();
        if (remainingTime < settings.minTime()) combatManager.stopCombat(user);
        else {
            ActionExecutor.execute(user.toPlayer(), messages.onTick(), Time.format((int) (remainingTime / 1000L)));
            user.updateBoardAndBar(remainingTime);
        }
    }
}
