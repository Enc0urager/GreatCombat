package dev.enco.greatcombat;

import dev.enco.greatcombat.commands.MainCommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.listeners.CombatListener;
import dev.enco.greatcombat.listeners.PlayerListener;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.utils.UpdateChecker;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class GreatCombat extends JavaPlugin {
    @Getter
    private static GreatCombat instance;
    private CombatManager combatManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        for (var player : Bukkit.getOnlinePlayers()) player.setInvisible(false);
        Logger.setup();
        MetaManager.setup();
        configManager = new ConfigManager(this);
        combatManager = new CombatManager();
        PowerupsManager.setServerManager(ConfigManager.getServerManager());
        var playerListener = new PlayerListener(combatManager);
        var combatListener = new CombatListener();
        var pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(combatListener, this);
        getCommand("greatcombat").setExecutor(new MainCommand());
        if (ConfigManager.isMetricsEnable()) {
            new Metrics(this, 25444);
        }
        Locale locale = ConfigManager.getLocale();
        Logger.info(locale.onEnable());
        Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        if (ConfigManager.isCheckUpdates()) {
            new UpdateChecker(this, version -> {
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
}
