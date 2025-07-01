package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.CombatEndEvent;
import dev.enco.greatcombat.api.CombatTickEvent;
import dev.enco.greatcombat.api.PlayerKickInCombatEvent;
import dev.enco.greatcombat.api.PlayerLeaveInCombatEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.utils.Time;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CombatListener implements Listener {
    private final Messages messages = ConfigManager.getMessages();
    private final Settings settings = ConfigManager.getSettings();
    private final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();

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
        ActionExecutor.execute(player, messages.onStop(), "", "");
        CooldownManager.clearPlayerCooldowns(player);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCombatQuit(PlayerLeaveInCombatEvent e) {
        var user = e.getUser();
        combatManager.stopCombat(user);
        var player = user.toPlayer();
        if (settings.killOnLeave() && !player.hasPermission("greatcombat.kill.bypass")) {
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
        var player = user.toPlayer();
        if (settings.killOnKick() && !player.hasPermission("greatcombat.kill.bypass")) {
            var kickmessages = settings.kickMessages();
            if (kickmessages.isEmpty() || kickmessages.contains(e.getReason())) {
                player.setHealth(0);
                ActionExecutor.execute(player, messages.onPvpLeave(), player.getName(), "");
            }
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
