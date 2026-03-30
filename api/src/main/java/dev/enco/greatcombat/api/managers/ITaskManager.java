package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IScheduler;
import org.bukkit.entity.Player;

public interface ITaskManager extends IManager {
    /**
     * Checks if plugin running on Folia
     *
     * @return true if a platform is Folia, false overwise
     */
    boolean isFolia();
    /**
     * Returns global task scheduler based on a server platform
     *
     * @return Folia or Bukkit scheduler
     */
    IScheduler getGlobalScheduler();
    /**
     * Returns entity-specific scheduler optimized for player-bound tasks.
     *
     * @param player Player to bind scheduler to
     * @return Entity scheduler for Folia, global scheduler for Bukkit
     */
    IScheduler getEntityScheduler(Player player);
}
