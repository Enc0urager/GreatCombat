package dev.enco.greatcombat.powerups;

import lombok.Getter;

/**
 * Enum representing different types of powerups that can be managed by the plugin.
 * Each powerup type has associated checker and disabler implementations for validation and management.
 * Powerups are abilities or states that can give players advantages in combat and may be
 * disabled or checked during combat sessions.
 *
 * @see Powerup
 * @see ServerManager
 */
@Getter
public enum PowerupType {
    FLY,
    GOD,
    VANISH,
    GAMEMODE,
    WALKSPEED;

    private Powerup powerup;

    /**
     * Initializes the powerup type with specific checker and disabler implementations
     * from the provided server manager. This method must be called for each powerup type
     * before using its checker or disabler functionality.
     *
     * @param serverManager The server manager implementation to provide checker and disabler instances.
     *
     * @see ServerManager
     */
    public void initialize(ServerManager serverManager) {
        switch (this) {
            case FLY:
                this.powerup = serverManager.flyPowerup();
                break;
            case GOD:
                this.powerup = serverManager.godPowerup();
                break;
            case VANISH:
                this.powerup = serverManager.vanishPowerup();
                break;
            case GAMEMODE:
                this.powerup = serverManager.gamemodePowerup();
                break;
            case WALKSPEED:
                this.powerup = serverManager.walkspeedPowerup();
                break;
        }
    }
}
