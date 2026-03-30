package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TownyListener implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @EventHandler
    public void listen(TownyPlayerDamagePlayerEvent event) {
        if (!configManager.getRegionWorlds().contains(event.getAttackingPlayer().getWorld().getName())) return;
        if (event.isCancelled()) {
            boolean damagerInCombat = combatManager.isInCombat(event.getAttackingPlayer().getUniqueId());
            boolean targetInCombat = combatManager.isInCombat(event.getVictimPlayer().getUniqueId());

            if (damagerInCombat && targetInCombat)
                event.setCancelled(false);
        }
    }
}
