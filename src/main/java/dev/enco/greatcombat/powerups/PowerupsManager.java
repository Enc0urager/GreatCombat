package dev.enco.greatcombat.powerups;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.powerups.impl.CMI;
import dev.enco.greatcombat.powerups.impl.EssentialsX;
import dev.enco.greatcombat.powerups.impl.Vanilla;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@UtilityClass @Getter
public class PowerupsManager {
    private ServerManager serverManager;

    public List<PowerupType> transform(List<String> pw) {
        List<PowerupType> pws = new ArrayList<>();
        for (var str : pw) pws.add(PowerupType.valueOf(str));
        return pws;
    }

    public void setServerManager(String manager) {
        var pm = Bukkit.getPluginManager();
        var smanager = GreatCombat.getInstance().getCombatManager();
        switch (manager) {
            case "CMI": {
                var cmi = new CMI(smanager);
                serverManager = cmi;
                pm.registerEvents(cmi, GreatCombat.getInstance());
                serverManager.setup();
                break;
            }
            case "Essentials": {
                var ess = new EssentialsX(smanager);
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

    public boolean hasPowerups(Player player, List<PowerupType> checks) {
        for (PowerupType check : checks) {
            if (check.getPowerupChecker().hasPowerup(player)) {
                return true;
            }
        }
        return false;
    }

    public void disablePowerups(Player player, List<PowerupType> checks) {
        for (PowerupType check : checks) {
            check.getPowerupDisabler().disablePowerup(player);
        }
    }
}
