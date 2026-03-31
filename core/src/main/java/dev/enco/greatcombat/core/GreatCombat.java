package dev.enco.greatcombat.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.enco.greatcombat.api.GreatCombatPlugin;
import dev.enco.greatcombat.api.GreatCombatProvider;
import dev.enco.greatcombat.api.managers.*;
import dev.enco.greatcombat.core.commands.MainCommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.listeners.CombatListener;
import dev.enco.greatcombat.core.listeners.PlayerListener;
import dev.enco.greatcombat.core.regions.RegionProvider;
import dev.enco.greatcombat.core.utils.PapiExpansion;
import dev.enco.greatcombat.core.utils.UpdateUtils;
import dev.enco.greatcombat.core.utils.logger.Logger;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class GreatCombat extends JavaPlugin implements GreatCombatPlugin {
    private ICombatManager combatManager;
    private IRegionManager regionManager;
    private ConfigManager configManager;
    private Injector injector;

    @Override
    public <T extends IManager> T getManager(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public void onEnable() {
        Logger.setup(this);
        injector = Guice.createInjector(new GreatCombatModule(this));
        configManager = injector.getInstance(ConfigManager.class);
        if (!configManager.checkAndCreateLangFiles()) return;
        configManager.load();
        combatManager = getManager(ICombatManager.class);
        PlayerListener playerListener = injector.getInstance(PlayerListener.class);
        CombatListener combatListener = injector.getInstance(CombatListener.class);
        var pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(combatListener, this);
        var command = getCommand("greatcombat");
        var cmd = new MainCommand(configManager, injector);
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);
        if (configManager.isMetricsEnable()) {
            new Metrics(this, 25444);
        }
        if (configManager.isUsingPapi()) {
            injector.getInstance(PapiExpansion.class).register();
        }
        regionManager = getManager(IRegionManager.class);
        try {
            String rm = configManager.getMainConfig().getString("region-manager");
            RegionProvider provider = RegionProvider.valueOf(rm.toUpperCase());
            regionManager.setListener(injector.getInstance(provider.getClazz()));
        } catch (IllegalArgumentException ignored) {}
        getManager(IInteractionManager.class).registerDefaults();
        Locale locale = configManager.getLocale();
        Logger.info(locale.onEnable());
        Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        if (configManager.isCheckUpdates()) {
            getManager(ITaskManager.class).getGlobalScheduler().runLaterAsync(() -> {
                UpdateUtils.check(locale, version -> {
                    if (getDescription().getVersion().equals(version)) {
                        Logger.info(locale.updatesNotFound());
                    } else {
                        for (var s : locale.updatesFound()) Logger.info(s);
                    }
                });
            }, 30L);
        }
        GreatCombatProvider.setPlugin(this);
    }

    @Override
    public void onDisable() {
        if (combatManager != null) combatManager.stop();
        Locale locale = configManager.getLocale();
        if (locale != null) {
            Logger.info(locale.onDisable());
            Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        }
    }
}
