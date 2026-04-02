package dev.enco.greatcombat.core.listeners;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.events.PlayerKickInCombatEvent;
import dev.enco.greatcombat.api.events.PlayerLeaveInCombatEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.actions.ActionMap;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Commands;
import dev.enco.greatcombat.core.config.settings.Messages;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerListener implements Listener {
    private final JavaPlugin plugin;
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private final ICooldownManager cooldownManager;
    private final IPreventionManager preventionManager;
    private final PluginManager pm = Bukkit.getPluginManager();

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player target) {
            var damager = getDamager(e.getDamager());
            if (damager != null) {
                combatManager.startCombat(damager, target);
            }
        }
        else if (e.getEntity() instanceof EnderCrystal crystal) {
            Player exploder = getDamager(e.getDamager());
            if (exploder != null) crystal.setMetadata("exploder", new FixedMetadataValue(plugin, exploder.getUniqueId()));
        }
    }

    private Player getDamager(Entity damager) {
        Settings settings = configManager.getSettings();
        if (damager instanceof Player pl) return pl;
        if (damager instanceof Projectile pr && pr.getShooter() instanceof Player pl)
            if (!settings.ignoredProjectile().contains(damager.getType())) return pl;
        if (damager instanceof AreaEffectCloud cl && cl.getSource() instanceof Player pl) return pl;
        if (damager instanceof TNTPrimed tnt && tnt.getSource() instanceof Player pl) return pl;
        if (damager instanceof EnderCrystal crystal && crystal.hasMetadata("exploder"))
            return Bukkit.getPlayer((UUID) crystal.getMetadata("exploder").get(0).value());
        return null;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        var user = combatManager.getUser(uuid);
        if (user != null) {
            pm.callEvent(new PlayerLeaveInCombatEvent(user));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        var user = combatManager.getUser(uuid);
        if (user != null) {
            pm.callEvent(new PlayerKickInCombatEvent(user,  ChatColor.stripColor(e.getReason())));
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPreprocess(PlayerCommandPreprocessEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) return;
        handlePlayerCommand(player, e.getMessage(), e);
        var uuid = player.getUniqueId();
        Commands commands = configManager.getCommands();
        Messages messages = configManager.getMessages();
        if (combatManager.isInCombat(uuid) && !commands.commands().isEmpty()) {
            String[] args = e.getMessage().split(" ");
            String command = args[0].substring(1).toLowerCase();
            String subCommand = args.length > 1 ? args[1].toLowerCase() : null;
            boolean cancel = false;
            boolean match = matchSubcommands(command, subCommand, commands);
            switch (commands.changeType()) {
                case BLACKLIST: {
                    if (match) cancel = true;
                    break;
                }
                case WHITELIST: {
                    if (!match) cancel = true;
                    break;
                }
            }
            if (cancel) {
                messages.onPvpCommand().execute(player);
                e.setCancelled(true);
            }
        }
    }

    private boolean matchSubcommands(String command, String subCommand, Commands commands) {
        var cmds = commands.commands();
        var subCmds = cmds.get(command);
        if (subCmds == null) return false;
        if (subCmds.isEmpty()) return true;
        if (subCommand == null) return false;
        return subCmds.contains(subCommand);
    }

    private void handlePlayerCommand(Player sender, String command, Cancellable e) {
        Commands commands = configManager.getCommands();
        Messages messages = configManager.getMessages();
        for (var s : commands.playerCommands()) {
            if (command.startsWith(s)) {
                try {
                    String targetName = command.replace(s, "").split(" ")[1];
                    Player player = Bukkit.getPlayer(targetName);
                    if (player != null && combatManager.isInCombat(player.getUniqueId())) {
                        e.setCancelled(true);
                        messages.onPlayerCommand().execute(sender, targetName);
                        break;
                    }
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e) {
        Settings settings = configManager.getSettings();
        if (settings.allowedTpCause().contains(e.getCause())) return;
        Player player = e.getPlayer();
        if (player.hasPermission("greatcombat.teleports.bypass")) return;
        if (!combatManager.isInCombat(player.getUniqueId())) return;
        e.setCancelled(true);
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onSend(PlayerCommandSendEvent e) {
        var player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) return;
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            Commands commands = configManager.getCommands();
            if (commands.changeComplete()) {
                var cmds = commands.commands();
                if (!cmds.isEmpty()) {
                    switch (commands.changeType()) {
                        case WHITELIST : {
                            e.getCommands().clear();
                            e.getCommands().addAll(cmds.keySet());
                            break;
                        }
                        case BLACKLIST : {
                            e.getCommands().removeAll(cmds.keySet());
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

    @EventHandler(ignoreCancelled = true)
    public void onHeld(PlayerItemHeldEvent e) {
        ActionMap actionMap = configManager.getMessages().onItemHeld();
        if (actionMap.isEmpty()) return;
        var player = e.getPlayer();
        var uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            var is = player.getInventory().getItem(e.getNewSlot());
            if (is == null) return;
            var item = cooldownManager.getCooldownItem(is);
            if (item == null) return;
            if (cooldownManager.hasCooldown(uuid, item)) {
                actionMap.execute(
                        player,
                        item.translation(),
                        Time.format(
                                cooldownManager.getCooldownTime(uuid, item)
                        )
                );
            }
        }
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        if (configManager.isElytraGlideAllowed()) return;
        if (!(e.getEntity() instanceof Player player)) return;
        if (player.hasPermission("greatcombat.glide.bypass")) return;
        var chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() != Material.ELYTRA) return;
        if (combatManager.isInCombat(player.getUniqueId())) e.setCancelled(true);
    }
}
