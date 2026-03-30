package dev.enco.greatcombat.core.powerups.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Vanilla implements PowerupProvider {
    private final ConfigManager configManager;

    @Inject
    public Vanilla(ConfigManager configManager) {
        this.configManager = configManager;
        setup();
    }

    @Override
    public void setup() {
        Logger.info(configManager.getLocale().serverManagerLoading().replace("{0}", "Vanilla"));
        setupFlyPowerup();
        setupGodPowerup();
        setupGamemodePowerup();
        setupVanishPowerup();
        setupWalkspeedPowerup();
    }

    private Powerup flyPowerup;

    private void setupFlyPowerup() {
        this.flyPowerup = DefaultPowerups.FLY_POWERUP;
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
        this.gamemodePowerup = DefaultPowerups.GM_POWERUP;
    }

    @Override
    public Powerup gamemodePowerup() {
        return this.gamemodePowerup;
    }

    private Powerup walkspeedPowerup;

    private void setupWalkspeedPowerup() {
        this.walkspeedPowerup = DefaultPowerups.WALKSPEED_POWERUP;
    }

    @Override
    public Powerup walkspeedPowerup() {
        return walkspeedPowerup;
    }
}
