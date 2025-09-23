package dev.enco.greatcombat.powerups.impl;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.powerups.Powerup;
import dev.enco.greatcombat.powerups.ServerManager;
import dev.enco.greatcombat.utils.logger.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Vanilla implements ServerManager {
    @Override
    public void setup() {
        Logger.info(ConfigManager.getLocale().serverManagerLoading().replace("{0}", "Vanilla"));
        setupFlyPowerup();
        setupGodPowerup();
        setupGamemodePowerup();
        setupVanishPowerup();
        setupWalkspeedPowerup();
    }

    private Powerup flyPowerup;

    private void setupFlyPowerup() {
        this.flyPowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.isFlying();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        };
    }

    @Override
    public Powerup flyPowerup() {
        return this.flyPowerup;
    }

    private Powerup godPowerup;

    private void setupGodPowerup() {
        this.godPowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.isInvulnerable();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setInvulnerable(false);
            }
        };
    }

    @Override
    public Powerup godPowerup() {
        return this.godPowerup;
    }

    private Powerup vanishPowerup;

    private void setupVanishPowerup() {
        this.vanishPowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.isInvisible();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setInvisible(false);
            }
        };
    }

    @Override
    public Powerup vanishPowerup() {
        return this.vanishPowerup;
    }

    private Powerup gamemodePowerup;

    private void setupGamemodePowerup() {
        this.gamemodePowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return !player.getGameMode().equals(GameMode.SURVIVAL);
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        };
    }

    @Override
    public Powerup gamemodePowerup() {
        return this.gamemodePowerup;
    }

    private Powerup walkspeedPowerup;

    private void setupWalkspeedPowerup() {
        this.walkspeedPowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.getWalkSpeed() != 0.2F;
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setWalkSpeed(0.2F);
            }
        };
    }

    @Override
    public Powerup walkspeedPowerup() {
        return walkspeedPowerup;
    }
}
