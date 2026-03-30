package dev.enco.greatcombat.core.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.core.scheduler.impl.BukkitScheduler;
import dev.enco.greatcombat.core.scheduler.impl.EntityScheduler;
import dev.enco.greatcombat.core.scheduler.impl.FoliaScheduler;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Task scheduler manager that automatically detects platform (Folia/Bukkit)
 * and provides appropriate scheduler implementations.
 */
@Singleton
public class TaskManager implements ITaskManager {
    private boolean IS_FOLIA;
    @Getter
    private final IScheduler globalScheduler;
    private final JavaPlugin plugin;

    @Inject
    public TaskManager(JavaPlugin plugin) {
        this.plugin = plugin;
        IScheduler scheduler;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            scheduler = new FoliaScheduler(plugin);
            IS_FOLIA = true;
        } catch (ClassNotFoundException e) {
            scheduler = new BukkitScheduler(plugin);
            IS_FOLIA = false;
        }
        globalScheduler = scheduler;
    }

    @Override
    public boolean isFolia() {
        return IS_FOLIA;
    }

    /**
     * Returns entity-specific scheduler optimized for player-bound tasks.
     *
     * @param player Player to bind scheduler to
     * @return Entity scheduler for Folia, global scheduler for Bukkit
     */
    @Override
    public IScheduler getEntityScheduler(Player player) {
        if (IS_FOLIA) return new EntityScheduler(plugin, player);
        return globalScheduler;
    }
}
