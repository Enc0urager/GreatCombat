package dev.enco.greatcombat.powerups.impl;

import dev.enco.greatcombat.powerups.PowerupChecker;
import dev.enco.greatcombat.powerups.PowerupDisabler;
import dev.enco.greatcombat.powerups.ServerManager;
import dev.enco.greatcombat.utils.Logger;
import org.bukkit.GameMode;

public class Vanilla implements ServerManager {
    @Override
    public void setup() {
        Logger.info("Используем Vanilla в качестве менеждера сервера");
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
        this.godDisabler = player -> player.setInvulnerable(false);
    }

    @Override
    public PowerupDisabler godDisabler() {
        return this.godDisabler;
    }

    private PowerupDisabler vanishDisabler;

    private void setupVanishDisabler() {
        this.vanishDisabler = player -> player.setInvisible(true);
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
        this.godChecker = player -> player.isInvulnerable();
    }

    @Override
    public PowerupChecker godChecker() {
        return this.godChecker;
    }

    private PowerupChecker vanishChecker;

    private void setupVanishChecker() {
        this.vanishChecker = player -> player.isInvisible();
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
}
