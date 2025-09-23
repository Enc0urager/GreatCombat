package dev.enco.greatcombat.scheduler;

/**
 * A generic wrapper interface for scheduled tasks that provides a unified way
 * to manage and cancel tasks across different scheduling implementations.
 *
 * @param <T> the type of the underlying task object
 */
public interface WrappedTask<T> {
    /**
     * Cancels this scheduled task
     */
    void cancel();

    /**
     * Returns the underlying task object
     *
     * @return the underlying task object (ScheduledTask for Folia, BukkitRunnable for Bukkit)
     * @see org.bukkit.scheduler.BukkitRunnable
     * @see io.papermc.paper.threadedregions.scheduler.ScheduledTask
     */
    T getRunnable();
}
