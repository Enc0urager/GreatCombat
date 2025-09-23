package dev.enco.greatcombat.scheduler;

import dev.enco.greatcombat.scheduler.impl.BukkitScheduler;
import dev.enco.greatcombat.scheduler.impl.EntityScheduler;
import dev.enco.greatcombat.scheduler.impl.FoliaScheduler;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * Task scheduler manager that automatically detects platform (Folia/Bukkit)
 * and provides appropriate scheduler implementations.
 */
@UtilityClass
public class TaskManager {
    public static boolean IS_FOLIA;
    @Getter
    private IScheduler globalScheduler;

    /**
     * Initializes scheduler system based on detected platform.
     */
    public void setup() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            globalScheduler = new FoliaScheduler();
            IS_FOLIA = true;
        } catch (ClassNotFoundException e) {
            globalScheduler = new BukkitScheduler();
            IS_FOLIA = false;
        }
    }

    /**
     * Returns entity-specific scheduler optimized for player-bound tasks.
     *
     * @param player Player to bind scheduler to
     * @return Entity scheduler for Folia, global scheduler for Bukkit
     */
    public IScheduler getEntityScheduler(Player player) {
        if (IS_FOLIA) return new EntityScheduler(player);
        return globalScheduler;
    }
}
