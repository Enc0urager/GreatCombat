package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.events.player.area.PlayerAreaEnterEvent;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class LandsListener implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private LandsIntegration api;

    @Override
    public void registerListener(JavaPlugin plugin) {
        api = LandsIntegration.of(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void listenEnter(PlayerAreaEnterEvent e) {
        if (!configManager.getRegionWorlds().contains(e.getLandPlayer().getPlayer().getWorld().getName())) return;
        Area area = e.getArea();
        LandPlayer landPlayer = e.getLandPlayer();
        Player player = landPlayer.getPlayer();
        UUID uuid = player.getUniqueId();
        if (combatManager.isInCombat(uuid)) {
            IUser user = combatManager.getUser(uuid);
            for (var opponent : user.getOpponents()) {
                LandPlayer lPlayer = api.getLandPlayer(opponent.getPlayerUUID());
                if (!area.canPvP(landPlayer, lPlayer, false)) {
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }
}
