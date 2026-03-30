package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.event.WrappedDisallowedPVPEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WorldGuardListener implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @Override
    public void registerListener(JavaPlugin plugin) {
        WorldGuardWrapper.getInstance().registerEvents(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void listen(WrappedDisallowedPVPEvent e) {
        if (!configManager.getRegionWorlds().contains(e.getAttacker().getWorld().getName())) return;
        boolean damagerInCombat = combatManager.isInCombat(e.getAttacker().getUniqueId());
        boolean targetInCombat = combatManager.isInCombat(e.getDefender().getUniqueId());

        if (damagerInCombat && targetInCombat) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }
}
