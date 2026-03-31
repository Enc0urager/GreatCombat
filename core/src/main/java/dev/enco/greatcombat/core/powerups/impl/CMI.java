package dev.enco.greatcombat.core.powerups.impl;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class CMI implements PowerupProvider {
    private PlayerManager playerManager;
    private com.Zrips.CMI.CMI cmi;
    private final ConfigManager configManager;

    public CMI(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setup() {
        final Locale locale = configManager.getLocale();
        Logger.info(MessageFormat.format(locale.serverManagerLoading(), "CMI"));
        long start = System.currentTimeMillis();
        try {
            cmi = com.Zrips.CMI.CMI.getInstance();
            playerManager = cmi.getPlayerManager();
            setupFlyPowerup();
            setupGodPowerup();
            setupGamemodePowerup();
            setupVanishPowerup();
            setupWalkspeedPowerup();
            Logger.info(MessageFormat.format(locale.serverManagerLoaded(), "CMI") + (System.currentTimeMillis() - start) + " ms.");
        } catch (Exception e) {
            Logger.error(locale.serverManagerError() + " CMI " + e);
        }
    }

    private Powerup flyPowerup;

    private void setupFlyPowerup() {
        this.flyPowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                CMIUser user = playerManager.getUser(player);
                return user.isFlying();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = playerManager.getUser(player);
                user.setFlying(false);
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
                CMIUser user = playerManager.getUser(player);
                return user.isGod();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = playerManager.getUser(player);
                cmi.getNMS().changeGodMode(player, false);
                user.setTgod(0L);
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
                CMIUser user = playerManager.getUser(player);
                return user.isVanished();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = playerManager.getUser(player);
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
        this.gamemodePowerup = new Powerup() {
            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return !player.getGameMode().equals(GameMode.SURVIVAL);
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = playerManager.getUser(player);
                user.setGameMode(GameMode.SURVIVAL);
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