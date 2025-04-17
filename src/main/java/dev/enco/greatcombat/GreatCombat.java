package dev.enco.greatcombat;

import dev.enco.greatcombat.commands.MainCommand;
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
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
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
        Logger.info("Плагин успешно включён!");
        Logger.info("Автор - Encourager, Версия " + this.getDescription().getVersion());
        if (ConfigManager.isCheckUpdates()) {
            new UpdateChecker(this, version -> {
                if (getDescription().getVersion().equals(version)) {
                    Logger.info("Вы используете последнюю версию плагина");
                } else {
                    Logger.info("Вы используете §cустаревшую §fверсию плагина!");
                    Logger.info("Вы можете скачать последнюю версию здесь:");
                    Logger.info("§ehttps://github.com/Enc0urager/GreatCombat/releases/");
                }
            });
        }
    }

    @Override
    public void onDisable() {
        combatManager.stop();
        Logger.info("Плагин успешно выключен");
        Logger.info("Автор - Encourager, Версия " + this.getDescription().getVersion());
    }
}
