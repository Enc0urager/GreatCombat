package dev.enco.greatcombat.powerups.impl;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import com.Zrips.CMI.events.CMIAsyncPlayerTeleportEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.manager.CombatManager;
import dev.enco.greatcombat.powerups.PowerupChecker;
import dev.enco.greatcombat.powerups.PowerupDisabler;
import dev.enco.greatcombat.powerups.ServerManager;
import dev.enco.greatcombat.utils.Logger;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CMI implements ServerManager, Listener {
    private PlayerManager playerManager;
    private com.Zrips.CMI.CMI cmi;
    private final CombatManager combatManager;

    @Override
    public void setup() {
        Logger.info("Подключаемся к CMI");
        long start = System.currentTimeMillis();
        try {
            cmi = com.Zrips.CMI.CMI.getInstance();
            playerManager = cmi.getPlayerManager();
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
            Logger.info("CMI подключен за " + (System.currentTimeMillis() - start) + " ms.");
        } catch (Exception e) {
            Logger.error("Unable to load CMI " + e);
        }
    }

    private PowerupDisabler flyDisabler;

    private void setupFlyDisabler() {
        this.flyDisabler = player -> {
            CMIUser user = playerManager.getUser(player);
            user.setFlying(false);
            user.setAllowFlight(false);
        };
    }

    @Override
    public PowerupDisabler flyDisabler() {
        return this.flyDisabler;
    }

    private PowerupDisabler godDisabler;

    private void setupGodDisabler() {
        this.godDisabler = player -> {
            CMIUser user = playerManager.getUser(player);
            cmi.getNMS().changeGodMode(player, false);
            user.setTgod(0L);
        };
    }

    @Override
    public PowerupDisabler godDisabler() {
        return this.godDisabler;
    }

    private PowerupDisabler vanishDisabler;

    private void setupVanishDisabler() {
        this.vanishDisabler = player -> {
            CMIUser user = playerManager.getUser(player);
            user.setVanished(false);
        };
    }


    @Override
    public PowerupDisabler vanishDisabler() {
        return this.vanishDisabler;
    }

    private PowerupDisabler gamemodeDisabler;

    private void setupGamemodeDisabler() {
        this.gamemodeDisabler = player -> {
            CMIUser user = playerManager.getUser(player);
            user.setGameMode(GameMode.SURVIVAL);
        };
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
        this.flyChecker = player -> {
            CMIUser user = playerManager.getUser(player);
            return user.isFlying();
        };
    }

    @Override
    public PowerupChecker flyChecker() {
        return this.flyChecker;
    }

    private PowerupChecker godChecker;

    private void setupGodChecker() {
        this.godChecker = player -> {
            CMIUser user = playerManager.getUser(player);
            return user.isGod();
        };
    }

    @Override
    public PowerupChecker godChecker() {
        return this.godChecker;
    }

    private PowerupChecker vanishChecker;

    private void setupVanishChecker() {
        this.vanishChecker = player -> {
            CMIUser user = playerManager.getUser(player);
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
    public void listenTeleports(CMIAsyncPlayerTeleportEvent e) {
        if (!ConfigManager.isTeleportEnable()) {
            Player player = e.getPlayer();
            if (player.hasPermission("greatcombat.teleports.bypass")) return;
            if (combatManager.isInCombat(player.getUniqueId())) e.setCancelled(true);
        }
    }
}