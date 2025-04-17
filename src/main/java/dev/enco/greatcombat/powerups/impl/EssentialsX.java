package dev.enco.greatcombat.powerups.impl;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.powerups.PowerupChecker;
import dev.enco.greatcombat.powerups.PowerupDisabler;
import dev.enco.greatcombat.powerups.ServerManager;
import dev.enco.greatcombat.utils.Logger;
import lombok.RequiredArgsConstructor;
import net.ess3.api.events.teleport.PreTeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class EssentialsX implements ServerManager, Listener {
    private Essentials essentials;
    private final CombatManager combatManager;

    @Override
    public void setup() {
        long start = System.currentTimeMillis();
        Logger.info("Подключаемся к Essentials");
        try {
            essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            setupFlyDisabler();
            setupFlyChecker();
            setupGodDisabler();
            setupGodChecker();
            setupGamemodeChecker();
            setupGamemodeDisabler();
            setupVanishChecker();
            setupVanishDisabler();
            setupWalkspeedChecker();
            setupWalkspeedDisabler();
            Logger.info("Essentials подключен за " + (System.currentTimeMillis() - start) + " ms.");
        } catch (Exception e) {
            Logger.error("Unable to load EssentialsX " + e);
        }
    }

    private PowerupDisabler flyDisabler;

    private void setupFlyDisabler() {
        this.flyDisabler = player -> {
            player.setFlying(false);
            player.setAllowFlight(false);
        };
    }

    @Override
    public PowerupDisabler flyDisabler() {
        return this.flyDisabler;
    }

    private PowerupDisabler godDisabler;

    private void setupGodDisabler() {
        this.godDisabler = player -> {
            User user = essentials.getUser(player);
            user.setGodModeEnabled(false);
        };
    }

    @Override
    public PowerupDisabler godDisabler() {
        return this.godDisabler;
    }

    private PowerupDisabler vanishDisabler;

    private void setupVanishDisabler() {
        this.vanishDisabler = player -> {
            User user = essentials.getUser(player);
            user.setVanished(false);
        };
    }

    @Override
    public PowerupDisabler vanishDisabler() {
        return this.vanishDisabler;
    }

    private PowerupDisabler gamemodeDisabler;

    private void setupGamemodeDisabler() {
        this.gamemodeDisabler = player -> player.setGameMode(GameMode.SURVIVAL);
    }

    @Override
    public PowerupDisabler gamemodeDisabler() {
        return this.gamemodeDisabler;
    }

    private PowerupDisabler walkspeedDisabler;

    private void setupWalkspeedDisabler() {
        this.walkspeedDisabler = player -> player.setWalkSpeed(0.2F);
    }

    @Override
    public PowerupDisabler walkspeedDisabler() {
        return this.walkspeedDisabler;
    }

    private PowerupChecker flyChecker;

    private void setupFlyChecker() {
        this.flyChecker = player -> player.isFlying();
    }

    @Override
    public PowerupChecker flyChecker() {
        return this.flyChecker;
    }

    private PowerupChecker godChecker;

    private void setupGodChecker() {
        this.godChecker = player -> {
            User user = essentials.getUser(player);
            return user.isGodModeEnabled();
        };
    }

    @Override
    public PowerupChecker godChecker() {
        return this.godChecker;
    }

    private PowerupChecker vanishChecker;

    private void setupVanishChecker() {
        this.vanishChecker = player -> {
            User user = essentials.getUser(player);
            return user.isVanished();
        };
    }

    @Override
    public PowerupChecker vanishChecker() {
        return this.vanishChecker;
    }

    private PowerupChecker gamemodeChecker;

    private void setupGamemodeChecker() {
        this.gamemodeChecker = player -> !player.getGameMode().equals(GameMode.SURVIVAL);
    }

    @Override
    public PowerupChecker gamemodeChecker() {
        return this.gamemodeChecker;
    }

    private PowerupChecker walkspeedChecker;

    private void setupWalkspeedChecker() {
        this.walkspeedChecker = player -> player.getWalkSpeed() != 0.2F;
    }

    @Override
    public PowerupChecker walkspeedChecker() {
        return walkspeedChecker;
    }

    @EventHandler
    public void listenTeleports(PreTeleportEvent e) {
        if (!ConfigManager.isTeleportEnable()) {
            Player player = e.getTeleporter().getBase();
            if (player.hasPermission("greatcombat.teleports.bypass")) return;
            if (combatManager.isInCombat(player.getUniqueId())) e.setCancelled(true);
        }
    }
}
