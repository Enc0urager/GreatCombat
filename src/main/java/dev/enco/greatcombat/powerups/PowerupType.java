package dev.enco.greatcombat.powerups;

import lombok.Getter;

/**
 * Enum representing different types of powerups that can be managed by the plugin.
 * Each powerup type has associated checker and disabler implementations for validation and management.
 * Powerups are abilities or states that can give players advantages in combat and may be
 * disabled or checked during combat sessions.
 *
 * @see PowerupChecker
 * @see PowerupDisabler
 * @see ServerManager
 */
@Getter
public enum PowerupType {
    FLY,
    GOD,
    VANISH,
    GAMEMODE,
    WALKSPEED;

    private PowerupChecker powerupChecker;
    private PowerupDisabler powerupDisabler;

    /**
     * Initializes the powerup type with specific checker and disabler implementations
     * from the provided server manager. This method must be called for each powerup type
     * before using its checker or disabler functionality.
     *
     * @param serverManager The server manager implementation to provide checker and disabler instances.
     *
     * @see ServerManager
     *
     * @deprecated This method is deprecated and will be removed in version 1.8
     */
    @Deprecated(since = "1.7.4")
    public void initialize(ServerManager serverManager) {
        switch (this) {
            case FLY:
                this.powerupDisabler = serverManager.flyDisabler();
                this.powerupChecker = serverManager.flyChecker();
                break;
            case GOD:
                this.powerupDisabler = serverManager.godDisabler();
                this.powerupChecker = serverManager.godChecker();
                break;
            case VANISH:
                this.powerupDisabler = serverManager.vanishDisabler();
                this.powerupChecker = serverManager.vanishChecker();
                break;
            case GAMEMODE:
                this.powerupDisabler = serverManager.gamemodeDisabler();
                this.powerupChecker = serverManager.gamemodeChecker();
                break;
            case WALKSPEED:
                this.powerupDisabler = serverManager.walkspeedDisabler();
                this.powerupChecker = serverManager.walkspeedChecker();
                break;
        }
    }
}
