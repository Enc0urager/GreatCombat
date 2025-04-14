package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Commands;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Powerups;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.utils.Time;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CombatListener implements Listener {
    private final Messages messages = ConfigManager.getMessages();
    private final Powerups powerups = ConfigManager.getPowerups();
    private final Settings settings = ConfigManager.getSettings();
    private final Commands commands = ConfigManager.getCommands();
    private final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onStartCombat(CombatStartEvent e) {
        var damager = e.getDamager();
        var target = e.getTarget();

        var damagerPlayer = damager.toPlayer();
        var targetPlayer = target.toPlayer();

        if (!damagerPlayer.hasPermission("greatcombat.powerups.bypass"))
            PowerupsManager.disablePowerups(damagerPlayer, powerups.disablingDamagerPowerups());
        if (!targetPlayer.hasPermission("greatcombat.powerups.bypass"))
            PowerupsManager.disablePowerups(targetPlayer, powerups.disablingTargetPowerups());

        ActionExecutor.execute(damagerPlayer, messages.onStartDamager(), targetPlayer.getName(), "");
        ActionExecutor.execute(targetPlayer, messages.onStartTarget(), damagerPlayer.getName(), "");
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
        ActionExecutor.execute(e.getUser().toPlayer(), messages.onStop(), "", "");
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatQuit(PlayerLeaveInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        if (settings.killOnLeave()) {
            var player = user.toPlayer();
            player.setHealth(0);
            ActionExecutor.execute(player, messages.onPvpLeave(), player.getName(), "");
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatKick(PlayerKickInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        if (settings.killOnKick()) {
            var kickmessages = settings.kickMessages();
            var player = user.toPlayer();
            if (kickmessages.isEmpty() || kickmessages.contains(e.getReason())) {
                player.setHealth(0);
                ActionExecutor.execute(player, messages.onPvpLeave(), player.getName(), "");
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatPreprocess(CommandPreprocessInCombatEvent e) {
        var user = e.getUser();
        boolean cancel = true;
        switch (commands.changeType()) {
            case BLACKLIST: {
                if (commands.commands().contains(e.getCommand())) {
                    cancel = true;
                }
                break;
            }
            case WHITELIST: {
                if (!commands.commands().contains(e.getCommand())) {
                    cancel = true;
                }
                break;
            }
        }
        if (cancel) {
            ActionExecutor.execute(user.toPlayer(), messages.onPvpCommand(), "", "");
            e.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatTick(CombatTickEvent e) {
        var user = e.getUser();
        long remainingTime = user.getRemaining();
        if (remainingTime < settings.minTime()) combatManager.stopCombat(user);
        else {
            ActionExecutor.execute(user.toPlayer(), messages.onTick(), Time.format((int) (remainingTime / 1000L)), "");
            user.updateBoardAndBar(remainingTime);
        }
    }
}
