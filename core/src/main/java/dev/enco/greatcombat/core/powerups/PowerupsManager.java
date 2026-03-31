package dev.enco.greatcombat.core.powerups;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IPowerupsManager;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.api.models.PowerupType;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.powerups.impl.CMI;
import dev.enco.greatcombat.core.powerups.impl.EssentialsX;
import dev.enco.greatcombat.core.powerups.impl.Vanilla;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumSet;

@Singleton
@Getter
@SuppressWarnings("unused")
public class PowerupsManager implements IPowerupsManager {
    private final ConfigManager configManager;
    private PowerupProvider provider;

    @Inject
    public PowerupsManager(ConfigManager configManager) {
        this.configManager = configManager;
        setServerManager(configManager.getServerManager());
    }

    @Override
    public void setPowerupProvider(PowerupProvider provider) {
        this.provider = provider;
        provider.setup();
        for (PowerupType type : PowerupType.values())
            type.initialize(provider);
    }

    public void setServerManager(String manager) {
        var pm = Bukkit.getPluginManager();
        setPowerupProvider(switch (manager) {
            case "CMI" -> new CMI(configManager);
            case "Essentials" -> new EssentialsX(configManager);
            default -> new Vanilla(configManager);
        });
    }

    @Override
    public boolean hasPowerups(Player player, EnumSet<PowerupType> checks) {
        if (player.hasPermission("greatcombat.powerups.bypass")) return false;

        for (PowerupType check : checks)
            if (check.getPowerup().hasPowerup(player)) return true;

        return false;
    }

    @Override
    public void disablePowerups(Player player, EnumSet<PowerupType> checks) {
        if (player.hasPermission("greatcombat.powerups.bypass")) return;
        for (PowerupType check : checks)
            check.getPowerup().disablePowerup(player);
    }
}
