package dev.enco.greatcombat.powerups;

/**
 * Interface defining the contract for server management systems that handle powerup operations.
 * Provides methods for setup and access to powerup checkers and disablers for various powerup types.
 *
 * @see Powerup
 */
public interface ServerManager {
    /**
     * Performs initial setup and configuration for the server manager implementation.
     * This method should be called before using any other methods in the interface.
     */
    void setup();
    /**
     * Gets the fly powerup implementation.
     *
     * @return Powerup for handling flight operations
     */
    Powerup flyPowerup();
    /**
     * Gets the god powerup implementation.
     *
     * @return Powerup for handling god operations
     */
    Powerup godPowerup();
    /**
     * Gets the vanish powerup implementation.
     *
     * @return Powerup for handling vanish operations
     */
    Powerup vanishPowerup();
    /**
     * Gets the gamemode powerup implementation.
     *
     * @return Powerup for handling gamemode operations
     */
    Powerup gamemodePowerup();
    /**
     * Gets the walkspeed powerup implementation.
     *
     * @return Powerup for handling walkspeed operations
     */
    Powerup walkspeedPowerup();
}
