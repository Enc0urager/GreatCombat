package dev.enco.greatcombat.powerups;

/**
 * Interface defining the contract for server management systems that handle powerup operations.
 * Provides methods for setup and access to powerup checkers and disablers for various powerup types.
 *
 * @see PowerupChecker
 * @see PowerupDisabler
 */
public interface ServerManager {
    /**
     * Performs initial setup and configuration for the server manager implementation.
     * This method should be called before using any other methods in the interface.
     */
    void setup();
    /**
     * Gets the fly powerup disabler implementation.
     *
     * @return PowerupDisabler for handling flight disabling operations
     */
    PowerupDisabler flyDisabler();
    /**
     * Gets the god powerup disabler implementation.
     *
     * @return PowerupDisabler for handling god disabling operations
     */
    PowerupDisabler godDisabler();
    /**
     * Gets the vanish powerup disabler implementation.
     *
     * @return PowerupDisabler for handling vanish disabling operations
     */
    PowerupDisabler vanishDisabler();
    /**
     * Gets the gamemode powerup disabler implementation.
     *
     * @return PowerupDisabler for handling gamemode disabling operations
     */
    PowerupDisabler gamemodeDisabler();
    /**
     * Gets the walkspeed powerup disabler implementation.
     *
     * @return PowerupDisabler for handling walkspeed disabling operations
     */
    PowerupDisabler walkspeedDisabler();
    /**
     * Gets the fly powerup checker implementation.
     *
     * @return PowerupChecker for checking flight status
     */
    PowerupChecker flyChecker();
    /**
     * Gets the god powerup checker implementation.
     *
     * @return PowerupChecker for checking god status
     */
    PowerupChecker godChecker();
    /**
     * Gets the vanish powerup checker implementation.
     *
     * @return PowerupChecker for checking vanish status
     */
    PowerupChecker vanishChecker();
    /**
     * Gets the gamemode powerup checker implementation.
     *
     * @return PowerupChecker for checking gamemode status
     */
    PowerupChecker gamemodeChecker();
    /**
     * Gets the walkspeed powerup checker implementation.
     *
     * @return PowerupChecker for checking walkspeed status
     */
    PowerupChecker walkspeedChecker();
}
