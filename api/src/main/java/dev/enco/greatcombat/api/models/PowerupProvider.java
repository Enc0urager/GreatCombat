package dev.enco.greatcombat.api.models;

import org.jetbrains.annotations.NotNull;

/**
 * Interface defining the contract for server management systems that handle powerup operations.
 * Provides methods for setup and access to powerup checkers and disablers for various powerup types.
 *
 * @see Powerup
 */
public interface PowerupProvider {
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
    @NotNull Powerup flyPowerup();
    /**
     * Gets the god powerup implementation.
     *
     * @return Powerup for handling god operations
     */
    @NotNull Powerup godPowerup();
    /**
     * Gets the vanish powerup implementation.
     *
     * @return Powerup for handling vanish operations
     */
    @NotNull Powerup vanishPowerup();
    /**
     * Gets the gamemode powerup implementation.
     *
     * @return Powerup for handling gamemode operations
     */
    @NotNull Powerup gamemodePowerup();
    /**
     * Gets the walkspeed powerup implementation.
     *
     * @return Powerup for handling walkspeed operations
     */
    @NotNull Powerup walkspeedPowerup();
}
