package dev.enco.greatcombat.api.models;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Extension of Bukkit {@link Listener} with utility methods
 * for simplified registration and unregistration.
 */
public interface IRegionListener extends Listener {
    /**
     * Registers this listener in the given plugin.
     *
     * @param plugin plugin instance
     */
    default void registerListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Unregisters this listener from all handlers.
     */
    default void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}
