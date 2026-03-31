package dev.enco.greatcombat.api;

import com.google.inject.ConfigurationException;
import dev.enco.greatcombat.api.managers.IManager;

/**
 * Core API interface representing the GreatCombat plugin.
 * <p>
 * Provides access to internal managers via a type-safe lookup mechanism.
 * This allows external modules to interact with the plugin without depending
 * on its internal implementation.
 */
public interface GreatCombatPlugin {
    /**
     * Retrieves a manager instance by its class type.
     *
     * @param clazz the class of the manager to retrieve
     * @param <T> the type of the manager
     * @return the manager instance associated with the given class
     * @throws ConfigurationException if the manager is not bound
     */
    <T extends IManager> T getManager(Class<T> clazz);
}
