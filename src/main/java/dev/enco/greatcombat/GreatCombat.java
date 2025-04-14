package dev.enco.greatcombat;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.listeners.CombatListener;
import dev.enco.greatcombat.listeners.PlayerListener;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.utils.Logger;
import dev.enco.greatcombat.utils.UpdateChecker;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class GreatCombat extends JavaPlugin {
    @Getter
    private static GreatCombat instance;
    private CombatManager combatManager;

    @Override
    public void onEnable() {
        instance = this;
        new ConfigManager(this);
        combatManager = new CombatManager();
        PowerupsManager.setServerManager(ConfigManager.getServerManager());
        var playerListener = new PlayerListener(combatManager);
        var combatListener = new CombatListener();
        var pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(combatListener, this);
        if (ConfigManager.isMetricsEnable()) {
            new Metrics(this, 25444);
        }
        Logger.info("Plugin successfully loaded!");
        Logger.info("Author - Encourager, Version " + this.getDescription().getVersion());
        new UpdateChecker(this, version -> {
            if (getDescription().getVersion().equals(version)) {
                Logger.info("You're using the latest version!");
            } else {
                Logger.info("You are using §cold §fversion of plugin!");
                Logger.info("You can download latest version here:");
                Logger.info("§ehttps://github.com/Enc0urager/GreatCombat/releases/");
            }
        });
    }

    @Override
    public void onDisable() {
        combatManager.stop();
        Logger.info("Plugin successfully disabled");
        Logger.info("Author - Encourager, Version " + this.getDescription().getVersion());
    }
}
