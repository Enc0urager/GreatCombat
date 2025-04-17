package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.*;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Commands;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.cooldowns.InteractionHandler;
import dev.enco.greatcombat.cooldowns.CooldownItem;
import dev.enco.greatcombat.cooldowns.CooldownManager;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.prevent.PreventableItem;
import dev.enco.greatcombat.prevent.PreventionManager;
import dev.enco.greatcombat.prevent.PreventionType;
import dev.enco.greatcombat.utils.Time;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final CombatManager combatManager;
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Commands commands = ConfigManager.getCommands();
    private final Messages messages = ConfigManager.getMessages();
    private final Settings settings = ConfigManager.getSettings();

    @EventHandler(
                priority = EventPriority.MONITOR
    )
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player target) {
            var damager = getDamager(e.getDamager());
            if (damager != null) {
                combatManager.startCombat(damager, target, e);
            }
        }
    }

    private Player getDamager(Entity damager) {
        if (damager instanceof Player pl) return pl;
        if (damager instanceof Projectile pr && pr.getShooter() instanceof Player pl)
            if (!settings.ignoredProjectile().contains(damager.getType())) return pl;
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
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var material = e.getItem().getType();
            var item = CooldownManager.getCooldownItem(material);
            if (item != null && item.handlers().contains(InteractionHandler.CONSUME)) {
                handleCooldown(uuid, player, item, e);
            }
            var preventable = PreventionManager.getPreventableItem(material);
            if (preventable != null && preventable.handlers().contains(InteractionHandler.CONSUME)) {
                handlePreventable(preventable, player, e);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            handleInteraction(e, player);
            handleBlockInteraction(e, player);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var is = player.getItemInHand();
            if (is == null) return;
            var material = is.getType();
            var item = CooldownManager.getCooldownItem(material);
            if (item != null) {
                if (item.handlers().contains(InteractionHandler.BLOCK_BREAK)) {
                    handleCooldown(uuid, player, item, e);
                }
            }
            var preventable = PreventionManager.getPreventableItem(material);
            if (preventable != null && preventable.handlers().contains(InteractionHandler.BLOCK_BREAK)) {
                handlePreventable(preventable, player, e);
            }
        }
    }

    @EventHandler
    public void onResurrect(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player player && !e.isCancelled()) {
            var uuid = player.getUniqueId();
            if (combatManager.isInCombat(uuid)) {
                var equipment = player.getEquipment();
                var itemInMain = equipment.getItemInMainHand();
                var itemInOff = equipment.getItemInOffHand();
                handleResurrect(e, player, equipment.getItemInMainHand(), InteractionHandler.RESURRECT_MAINHAND, uuid);
                if (!itemInOff.getType().equals(itemInMain.getType()))
                    handleResurrect(e, player, equipment.getItemInOffHand(), InteractionHandler.RESURRECT_OFFHAND, uuid);
            }
        }
    }

    private void handleResurrect(EntityResurrectEvent e, Player player, ItemStack item, InteractionHandler handler, UUID uuid) {
        if (item != null) {
            var material = item.getType();
            var preventableItem = PreventionManager.getPreventableItem(material);
            if (preventableItem != null && preventableItem.handlers().contains(handler)) {
                handlePreventable(preventableItem, player, e);
            }
            var cooldownItem = CooldownManager.getCooldownItem(material);
            if (cooldownItem != null && cooldownItem.handlers().contains(handler)) {
                handleCooldown(uuid, player, cooldownItem, e);
            }
        }
    }

    private void handleBlockInteraction(PlayerInteractEvent e, Player player) {
        var blockMaterial = e.getClickedBlock() != null ? e.getClickedBlock().getType() : null;
        if (blockMaterial != null) {
            var preventable = PreventionManager.getPreventableItem(blockMaterial);
            if (preventable != null && preventable.types().contains(PreventionType.INTERACTED_BLOCK)) {
                var action = e.getAction();
                if (shouldBlockAction(action, preventable.handlers())) {
                    handlePreventable(preventable, player, e);
                }
            }
        }
    }

    private void handleInteraction(PlayerInteractEvent e, Player player) {
        ItemStack is = e.getItem();
        if (is == null) return;
        var material = is.getType();
        var preventable = PreventionManager.getPreventableItem(material);
        var action = e.getAction();
        if (preventable != null && preventable.types().contains(PreventionType.INTERACTED_ITEM)) {
            if (shouldBlockAction(action, preventable.handlers())) {
                handlePreventable(preventable, player, e);
            }
        }
        var item = CooldownManager.getCooldownItem(material);
        if (item != null) {
            if (shouldBlockAction(action, item.handlers())) {
                handleCooldown(player.getUniqueId(), player, item, e);
            }
        }
    }

    private boolean shouldBlockAction(Action action, List<InteractionHandler> handlers) {
        switch (action) {
            case RIGHT_CLICK_AIR:
                return handlers.contains(InteractionHandler.RIGHT_CLICK_AIR);
            case RIGHT_CLICK_BLOCK:
                return handlers.contains(InteractionHandler.RIGHT_CLICK_BLOCK);
            case LEFT_CLICK_AIR:
                return handlers.contains(InteractionHandler.LEFT_CLICK_AIR);
            case LEFT_CLICK_BLOCK:
                return handlers.contains(InteractionHandler.LEFT_CLICK_BLOCK);
            default:
                return false;
        }
    }

    private void handlePreventable(PreventableItem preventable, Player player, Cancellable e) {
        if (player.hasPermission("greatcombat.prevention.bypass")) return;
        ActionExecutor.execute(player, messages.onInteract(), preventable.translation(), "");
        e.setCancelled(true);
    }

    private void handleCooldown(UUID uuid, Player player, CooldownItem item, Cancellable e) {
        if (player.hasPermission("greatcombat.cooldowns.bypass")) return;
        if (CooldownManager.hasCooldown(uuid, item)) {
            int time = CooldownManager.getCooldownTime(uuid, item);
            ActionExecutor.execute(player, messages.onItemCooldown(), Time.format(time), item.translation());
            e.setCancelled(true);
        } else CooldownManager.putCooldown(uuid, item);
    }
}
