package dev.enco.greatcombat.core;

import com.google.inject.AbstractModule;
import dev.enco.greatcombat.api.GreatCombatPlugin;
import dev.enco.greatcombat.api.managers.*;
import dev.enco.greatcombat.core.manager.CombatManager;
import dev.enco.greatcombat.core.powerups.PowerupsManager;
import dev.enco.greatcombat.core.regions.RegionManager;
import dev.enco.greatcombat.core.restrictions.InteractionManager;
import dev.enco.greatcombat.core.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.core.restrictions.meta.MetaManager;
import dev.enco.greatcombat.core.restrictions.prevention.PreventionManager;
import dev.enco.greatcombat.core.scheduler.TaskManager;
import dev.enco.greatcombat.core.scoreboard.ScoreboardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GreatCombatModule extends AbstractModule {
    private final JavaPlugin plugin;

    public GreatCombatModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(ITaskManager.class).to(TaskManager.class);
        bind(ICooldownManager.class).to(CooldownManager.class);
        bind(IScoreboardManager.class).to(ScoreboardManager.class);
        bind(IPreventionManager.class).to(PreventionManager.class);
        bind(IMetaManager.class).to(MetaManager.class);
        bind(IPowerupsManager.class).to(PowerupsManager.class);
        bind(ICombatManager.class).to(CombatManager.class);
        bind(IRegionManager.class).to(RegionManager.class);
        bind(GreatCombatPlugin.class).to(GreatCombat.class);
        bind(IInteractionManager.class).to(InteractionManager.class);
    }
}
