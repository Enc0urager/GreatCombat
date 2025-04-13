package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Commands;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.cooldowns.CooldownHandler;
import dev.enco.greatcombat.cooldowns.CooldownItem;
import dev.enco.greatcombat.cooldowns.CooldownManager;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.utils.Time;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final CombatManager combatManager;
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Commands commands = ConfigManager.getCommands();
    private final Messages messages = ConfigManager.getMessages();

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player target) {
            var damager = getDamager(e.getDamager());
            if (damager != null) {
                combatManager.startCombat(damager, target, e);
            }
        }
    }

    private Player getDamager(Entity damager) {
        if (damager instanceof Player pl) return pl;
        if (damager instanceof Projectile pr && pr.getShooter() instanceof Player pl) return pl;
        if (damager instanceof AreaEffectCloud cl && cl.getSource() instanceof Player pl) return pl;
        if (damager instanceof TNTPrimed tnt && tnt.getSource() instanceof Player pl) return pl;
        return null;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.kill.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            pm.callEvent(new PlayerLeaveInCombatEvent(combatManager.getUser(uuid)));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.kill.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            pm.callEvent(new PlayerKickInCombatEvent(combatManager.getUser(uuid), ChatColor.stripColor(e.getReason())));
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPreprocess(PlayerCommandPreprocessEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var command = e.getMessage().split(" ")[0].replaceFirst("/", "");
            if (!commands.commands().isEmpty()) {
                pm.callEvent(new CommandPreprocessInCombatEvent(combatManager.getUser(uuid), command, e));
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onSend(PlayerCommandSendEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) return;
        if (commands.changeComplete()) {
            var uuid = player.getUniqueId();
            if (combatManager.isInCombat(uuid)) {
                var cmds = commands.commands();
                if (!cmds.isEmpty()) {
                    switch (commands.changeType()) {
                        case WHITELIST : {
                            e.getCommands().clear();
                            e.getCommands().addAll(cmds);
                            break;
                        }
                        case BLACKLIST : {
                            e.getCommands().removeAll(cmds);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            combatManager.stopCombat(combatManager.getUser(uuid));
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.cooldowns.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var material = e.getItem().getType();
            var item = CooldownManager.getCooldownItem(material);
            if (item != null) {
                if (item.handlers().contains(CooldownHandler.CONSUME)) {
                    handleCooldown(uuid, player, item, e);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.cooldowns.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            ItemStack is = e.getItem();
            if (is == null) return;
            var material = is.getType();
            var item = CooldownManager.getCooldownItem(material);
            if (item != null) {
                var action = e.getAction();
                switch (action) {
                    case RIGHT_CLICK_AIR: {
                        if (item.handlers().contains(CooldownHandler.RIGHT_CLICK_AIR)) {
                            handleCooldown(uuid, player, item, e);
                        }
                        break;
                    }
                    case RIGHT_CLICK_BLOCK: {
                        if (item.handlers().contains(CooldownHandler.RIGHT_CLICK_BLOCK)) {
                            handleCooldown(uuid, player, item, e);
                        }
                        break;
                    }
                    case LEFT_CLICK_AIR: {
                        if (item.handlers().contains(CooldownHandler.LEFT_CLICK_AIR)) {
                            handleCooldown(uuid, player, item, e);
                        }
                        break;
                    }
                    case LEFT_CLICK_BLOCK: {
                        if (item.handlers().contains(CooldownHandler.LEFT_CLICK_BLOCK)) {
                            handleCooldown(uuid, player, item, e);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.cooldowns.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var is = player.getItemInHand();
            if (is == null) return;
            var material = is.getType();
            var item = CooldownManager.getCooldownItem(material);
            if (item != null) {
                if (item.handlers().contains(CooldownHandler.BLOCK_BREAK)) {
                    handleCooldown(uuid, player, item, e);
                }
            }
        }
    }

    private void handleCooldown(UUID uuid, Player player, CooldownItem item, Cancellable e) {
        if (CooldownManager.hasCooldown(uuid, item)) {
            int time = CooldownManager.getCooldownTime(uuid, item);
            ActionExecutor.execute(player, messages.onItemCooldown(), Time.format(time), item.translation());
            e.setCancelled(true);
        } else CooldownManager.putCooldown(uuid, item);
    }
}
