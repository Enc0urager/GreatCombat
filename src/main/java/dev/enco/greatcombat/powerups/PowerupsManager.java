package dev.enco.greatcombat.powerups;

import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.powerups.impl.CMI;
import dev.enco.greatcombat.powerups.impl.EssentialsX;
import dev.enco.greatcombat.powerups.impl.Vanilla;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@UtilityClass @Getter
public class PowerupsManager {
    private ServerManager serverManager;

    public List<PowerupType> transform(List<String> pw) {
        final Locale locale = ConfigManager.getLocale();
        List<PowerupType> pws = new ArrayList<>();
        for (var str : pw) {
            try {
                pws.add(PowerupType.valueOf(str));
            } catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(locale.powerupTypeDoesNotExist(), str));
            }
        }
        return pws;
    }

    public void setServerManager(ServerManager manager) {
        serverManager = manager;
    }

    public void setServerManager(String manager) {
        var pm = Bukkit.getPluginManager();
        var combatManager = GreatCombat.getInstance().getCombatManager();
        switch (manager) {
            case "CMI": {
                var cmi = new CMI(combatManager);
                serverManager = cmi;
                pm.registerEvents(cmi, GreatCombat.getInstance());
                serverManager.setup();
                break;
            }
            case "Essentials": {
                var ess = new EssentialsX(combatManager);
                serverManager = ess;
                pm.registerEvents(ess, GreatCombat.getInstance());
                serverManager.setup();
                break;
            }
            default: {
                serverManager = new Vanilla();
                serverManager.setup();
                break;
            }
        }
        for (PowerupType type : PowerupType.values()) {
            type.initialize(serverManager);
        }
    }

    public boolean hasPowerups(Player player, ImmutableSet<PowerupType> checks) {
        for (PowerupType check : checks) {
            if (check.getPowerupChecker().hasPowerup(player)) {
                return true;
            }
        }
        return false;
    }

    public void disablePowerups(Player player, ImmutableSet<PowerupType> checks) {
        for (PowerupType check : checks) {
            check.getPowerupDisabler().disablePowerup(player);
        }
    }
}
