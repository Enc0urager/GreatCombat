package dev.enco.greatcombat;

import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import dev.enco.greatcombat.commands.MainCommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.listeners.CombatListener;
import dev.enco.greatcombat.listeners.PlayerListener;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.manager.User;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.scheduler.TaskManager;
import dev.enco.greatcombat.utils.UpdateUtils;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import me.angeschossen.lands.api.events.player.area.PlayerAreaEnterEvent;
import me.angeschossen.lands.api.handler.APIHandler;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.event.WrappedDisallowedPVPEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public final class GreatCombat extends JavaPlugin {
    @Getter
    private static GreatCombat instance;
    private CombatManager combatManager;
    private ConfigManager configManager;
    private final Set<String> worlds = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        Logger.setup();
        TaskManager.setup();
        MetaManager.setup();
        configManager = new ConfigManager(this);
        if (!configManager.checkAndCreateLangFiles()) return;
        configManager.load();
        combatManager = new CombatManager();
        PowerupsManager.setServerManager(ConfigManager.getServerManager());
        var playerListener = new PlayerListener(combatManager);
        var combatListener = new CombatListener();
        var pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(combatListener, this);
        FileConfiguration config = ConfigManager.getMainConfig();
        setupRegionProvider(pm, config.getString("region-manager"));
        worlds.addAll(config.getStringList("region-worlds"));
        var command = getCommand("greatcombat");
        var cmd = new MainCommand();
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);
        if (ConfigManager.isMetricsEnable()) {
            new Metrics(this, 25444);
        }
        Locale locale = ConfigManager.getLocale();
        Logger.info(locale.onEnable());
        Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        if (ConfigManager.isCheckUpdates()) {
            UpdateUtils.check(version -> {
                if (getDescription().getVersion().equals(version)) {
                    Logger.info(locale.updatesNotFound());
                } else {
                    for (var s : locale.updatesFound()) Logger.info(s);
                }
            });
        }
    }

    @Override
    public void onDisable() {
        if (combatManager != null) combatManager.stop();
        Locale locale = ConfigManager.getLocale();
        if (locale != null) {
            Logger.info(locale.onDisable());
            Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        }
    }

    private void setupRegionProvider(PluginManager pm, String provider) {
        Listener listener = null;
        switch (provider) {
            case "WorldGuard": {
                WorldGuardWrapper.getInstance().registerEvents(this);
                listener = new Listener() {
                    @EventHandler
                    public void onDamage(WrappedDisallowedPVPEvent event) {
                        if (!worlds.contains(event.getAttacker().getWorld().getName())) return;
                        boolean damagerInCombat = combatManager.isInCombat(event.getAttacker().getUniqueId());
                        boolean targetInCombat = combatManager.isInCombat(event.getDefender().getUniqueId());

                        if (damagerInCombat && targetInCombat) {
                            event.setCancelled(true);
                            event.setResult(Event.Result.DENY);
                        }
                    }
                };
                break;
            }
            case "Towny": {
                listener = new Listener() {
                    @EventHandler(
                            priority = EventPriority.HIGHEST
                    )
                    public void listenDamage(TownyPlayerDamagePlayerEvent event) {
                        if (!worlds.contains(event.getAttackingPlayer().getWorld().getName())) return;
                        if (event.isCancelled()) {
                            boolean damagerInCombat = combatManager.isInCombat(event.getAttackingPlayer().getUniqueId());
                            boolean targetInCombat = combatManager.isInCombat(event.getVictimPlayer().getUniqueId());

                            if (damagerInCombat && targetInCombat)
                                event.setCancelled(false);
                        }
                    }
                };
                break;
            }
            case "Lands": {
                LandsIntegration api = LandsIntegration.of(GreatCombat.getInstance());
                listener = new Listener() {
                    @EventHandler
                    public void listenEnter(PlayerAreaEnterEvent e) {
                        if (!worlds.contains(e.getLandPlayer().getPlayer().getWorld().getName())) return;
                        Area area = e.getArea();
                        LandPlayer landPlayer = e.getLandPlayer();
                        Player player = landPlayer.getPlayer();
                        UUID uuid = player.getUniqueId();
                        if (combatManager.isInCombat(uuid)) {
                            User user = combatManager.getUser(uuid);
                            for (var opponent : user.getOpponents()) {
                                LandPlayer lPlayer = api.getLandPlayer(opponent.getPlayerUUID());
                                if (!area.canPvP(landPlayer, lPlayer, false)) {
                                    e.setCancelled(true);
                                    break;
                                }
                            }
                        }
                    }
                };
                break;
            }
            default: break;
        }
        if (listener != null) pm.registerEvents(listener, this);
    }
}
