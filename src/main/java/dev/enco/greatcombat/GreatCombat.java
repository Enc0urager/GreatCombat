package dev.enco.greatcombat;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.listeners.CombatListener;
import dev.enco.greatcombat.listeners.PlayerListener;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.utils.Logger;
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
        Logger.info("Плагин успешно загружен!");
        Logger.info("Автор - Encourager, Версия " + this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        combatManager.stop();
        Logger.info("Плагин успешно выключен");
        Logger.info("Разработчик - Encourager, Версия " + this.getDescription().getVersion());
    }
}
