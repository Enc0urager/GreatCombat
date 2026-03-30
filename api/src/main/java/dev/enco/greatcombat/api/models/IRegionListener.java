package dev.enco.greatcombat.api.models;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface IRegionListener extends Listener {
    default void registerListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    default void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}
