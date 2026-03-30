package dev.enco.greatcombat.core.listeners;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.events.*;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IPowerupsManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Messages;
import dev.enco.greatcombat.core.config.settings.Powerups;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CombatListener implements Listener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private final IPowerupsManager powerupsManager;
    private final IScoreboardManager scoreboardManager;
    private final ICooldownManager cooldownManager;

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onPreStart(CombatPreStartEvent e) {
        Player damager = e.getDamager();
        Player target = e.getTarget();

        Settings settings = configManager.getSettings();

        if (settings.ignoredWorlds().contains(damager.getWorld().getName())) {
            e.setCancelled(true);
            return;
        }

        Powerups powerups = configManager.getPowerups();
        if (powerupsManager.hasPowerups(damager, powerups.preventableDamagerPowerups())
                || powerupsManager.hasPowerups(target, powerups.preventableTargetPowerups()))
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
        IUser user = e.isDamagerJoiner() ? e.getTarget() : e.getDamager();
        IUser joiner = e.isDamagerJoiner() ? e.getDamager() : e.getTarget();
        addBoth(joiner, user);
        Player userPlayer = user.asPlayer();
        long now = System.currentTimeMillis();
        user.refresh(now);
        Messages messages = configManager.getMessages();
        messages.onJoin().execute(joiner.asPlayer(), userPlayer.getName());
        Powerups powerups = configManager.getPowerups();
        processStart(joiner, powerups, now, e.getDamager().getPlayerUUID().equals(joiner.getPlayerUUID()));
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onStart(CombatStartEvent e) {
        IUser damager = e.getDamager();
        IUser target = e.getTarget();
        addBoth(damager, target);
        long now = System.currentTimeMillis();

        Powerups powerups = configManager.getPowerups();
        processStart(damager, powerups, now, true);
        processStart(target, powerups, now, false);

        Messages messages = configManager.getMessages();
        Player damagerPlayer = damager.asPlayer();
        Player targetPlayer = target.asPlayer();
        messages.onStartDamager().execute(damagerPlayer, targetPlayer.getName());
        messages.onStartTarget().execute(targetPlayer, damagerPlayer.getName());
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onMerge(CombatMergeEvent e) {
        IUser damager = e.getDamager();
        IUser target = e.getTarget();
        addBoth(damager, target);
        long now = System.currentTimeMillis();
        damager.refresh(now);
        target.refresh(now);
        Player damagerPlayer = damager.asPlayer();
        Player targetPlayer = target.asPlayer();
        Messages messages = configManager.getMessages();
        messages.onMerge().execute(damagerPlayer, targetPlayer.getName());
        messages.onMerge().execute(targetPlayer, damagerPlayer.getName());
    }

    private void processStart(IUser user, Powerups powerups, long now, boolean damager) {
        Player player = user.asPlayer();
        powerupsManager.disablePowerups(player, damager ? powerups.disablingDamagerPowerups() : powerups.disablingTargetPowerups());
        user.setStartPvpTime(now);
        user.startTimer();
    }

    private void addBoth(IUser user1, IUser user2) {
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
        scoreboardManager.resetScoreboard(user);
        combatManager.removeFromCombatMap(user);
        var player = user.asPlayer();
        Messages messages = configManager.getMessages();
        messages.onStop().execute(player);
        cooldownManager.clearPlayerCooldowns(player);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatQuit(PlayerLeaveInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        var player = user.asPlayer();
        Messages messages = configManager.getMessages();
        Settings settings = configManager.getSettings();
        if (settings.killOnLeave() && !player.hasPermission("greatcombat.kill.bypass")) {
            player.setHealth(0);
            messages.onPvpLeave().execute(player, player.getName());
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatKick(PlayerKickInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        var player = user.asPlayer();
        Messages messages = configManager.getMessages();
        Settings settings = configManager.getSettings();
        if (settings.killOnKick() && !player.hasPermission("greatcombat.kill.bypass")) {
            var kickmessages = settings.kickMessages();
            if (kickmessages.isEmpty() || kickmessages.contains(e.getReason())) {
                player.setHealth(0);
                messages.onPvpLeave().execute(player, player.getName());
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatTick(CombatTickEvent e) {
        var user = e.getUser();
        long remainingTime = user.getRemaining();
        Messages messages = configManager.getMessages();
        Settings settings = configManager.getSettings();
        if (remainingTime < settings.minTime()) combatManager.stopCombat(user);
        else {
            messages.onTick().execute(user.asPlayer(), Time.format((int) (remainingTime / 1000L)));
            user.updateBoardAndBar(remainingTime);
        }
    }
}
