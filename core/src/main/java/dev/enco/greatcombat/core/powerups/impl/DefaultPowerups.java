package dev.enco.greatcombat.core.powerups.impl;

import dev.enco.greatcombat.api.models.Powerup;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@Getter
public class DefaultPowerups {
    public static final Powerup FLY_POWERUP = new Powerup() {
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

    public static final Powerup GM_POWERUP = new Powerup() {
        @Override
        public boolean hasPowerup(@NotNull Player player) {
            return !player.getGameMode().equals(GameMode.SURVIVAL);
        }

        @Override
        public void disablePowerup(@NotNull Player player) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    };

    public static final Powerup WALKSPEED_POWERUP = new Powerup() {
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
