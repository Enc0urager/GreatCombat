package dev.enco.greatcombat.powerups;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PowerupDisabler {
    void disablePowerup(@NotNull Player player);
}
