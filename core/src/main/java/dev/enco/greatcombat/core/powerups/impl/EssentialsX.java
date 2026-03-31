package dev.enco.greatcombat.core.powerups.impl;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.google.inject.Inject;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class EssentialsX implements PowerupProvider {
    private final ConfigManager configManager;
    private Essentials essentials;

    @Inject
    public EssentialsX(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setup() {
        final Locale locale = configManager.getLocale();
        long start = System.currentTimeMillis();
        Logger.info(MessageFormat.format(locale.serverManagerLoading(), "EssentialsX"));
        try {
            essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            setupFlyPowerup();
            setupGodPowerup();
            setupGamemodePowerup();
            setupVanishPowerup();
            setupWalkspeedPowerup();
            Logger.info(MessageFormat.format(locale.serverManagerLoaded(), "EssentialsX") + (System.currentTimeMillis() - start) + " ms.");
        } catch (Exception e) {
            Logger.error(locale.serverManagerError() + " EssentialsX " + e);
        }
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
                User user = essentials.getUser(player);
                return user.isGodModeEnabled();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                User user = essentials.getUser(player);
                user.setGodModeEnabled(false);
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
                User user = essentials.getUser(player);
                return user.isVanished();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                User user = essentials.getUser(player);
                user.setVanished(false);
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
