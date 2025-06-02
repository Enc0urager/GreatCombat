package dev.enco.greatcombat.listeners;

import dev.enco.greatcombat.actions.ActionExecutor;
import dev.enco.greatcombat.api.PlayerKickInCombatEvent;
import dev.enco.greatcombat.api.PlayerLeaveInCombatEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Commands;
import dev.enco.greatcombat.config.settings.Messages;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.restrictions.cooldowns.CooldownItem;
import dev.enco.greatcombat.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.restrictions.prevention.PreventableItem;
import dev.enco.greatcombat.restrictions.prevention.PreventionManager;
import dev.enco.greatcombat.restrictions.prevention.PreventionType;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import java.util.EnumSet;
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
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            pm.callEvent(new PlayerLeaveInCombatEvent(combatManager.getUser(uuid)));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        var player = e.getPlayer();
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
        if (combatManager.isInCombat(uuid) && !commands.commands().isEmpty()) {
            var command = e.getMessage().split(" ")[0].replaceFirst("/", "");
            boolean cancel = true;
            switch (commands.changeType()) {
                case BLACKLIST: {
                    if (commands.commands().contains(command)) {
                        cancel = true;
                    }
                    break;
                }
                case WHITELIST: {
                    if (!commands.commands().contains(command)) {
                        cancel = true;
                    }
                    break;
                }
            }
            if (cancel) {
                ActionExecutor.execute(player, messages.onPvpCommand(), "", "");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onSend(PlayerCommandSendEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            if (commands.changeComplete()) {
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
            var is = e.getItem();
            check(is, uuid, player, e, InteractionHandler.CONSUME);
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
            check(is, uuid, player, e, InteractionHandler.BLOCK_BREAK);
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
                check(itemInMain, uuid, player, e, InteractionHandler.RESURRECT_MAINHAND);
                if (!itemInOff.getType().equals(itemInMain.getType()))
                    check(itemInOff, uuid, player, e, InteractionHandler.RESURRECT_OFFHAND);
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player player) {
            var uuid = player.getUniqueId();
            if (combatManager.isInCombat(uuid)) {
                var is = e.getBow();
                check(is, uuid, player, e, InteractionHandler.BOW_SHOOT);
            }
        }
    }

    private void handleBlockInteraction(PlayerInteractEvent e, Player player) {
        var blockMaterial = e.getClickedBlock() != null ? e.getClickedBlock().getType() : null;
        if (blockMaterial != null) {
            var preventable = PreventionManager.getPreventableItem(new ItemStack(blockMaterial));
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
        var preventable = PreventionManager.getPreventableItem(is);
        var action = e.getAction();
        if (preventable != null && preventable.types().contains(PreventionType.INTERACTED_ITEM)) {
            if (shouldBlockAction(action, preventable.handlers())) {
                handlePreventable(preventable, player, e);
            }
        }
        var item = CooldownManager.getCooldownItem(is);
        if (item != null) {
            if (shouldBlockAction(action, item.handlers())) {
                handleCooldown(player.getUniqueId(), player, item, e);
            }
        }
    }

    private boolean shouldBlockAction(Action action, EnumSet<InteractionHandler> handlers) {
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

    private void check(ItemStack itemStack, UUID uuid, Player player, Cancellable e, InteractionHandler handler) {
        if (itemStack == null) return;
        var item = CooldownManager.getCooldownItem(itemStack);
        if (item != null && item.handlers().contains(handler)) {
            handleCooldown(uuid, player, item, e);
        }
        var preventable = PreventionManager.getPreventableItem(itemStack);
        if (preventable != null && preventable.handlers().contains(handler)) {
            handlePreventable(preventable, player, e);
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
        } else CooldownManager.putCooldown(uuid, player, item);
    }
}
