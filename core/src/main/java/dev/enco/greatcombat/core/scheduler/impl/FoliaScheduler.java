package dev.enco.greatcombat.core.scheduler.impl;

import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.scheduler.tasks.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements IScheduler {
    private final JavaPlugin plugin;
    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
        globalScheduler = plugin.getServer().getGlobalRegionScheduler();
        asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    @Override
    public void run(@NotNull Runnable task) {
        globalScheduler.run(
                plugin,
                t -> task.run()
        );
    }

    @Override
    public void runAsync(@NotNull Runnable task) {
        asyncScheduler.runNow(
                plugin,
                t -> task.run()
        );
    }

    @Override
    public void runLater(@NotNull Runnable task, long delay) {
        globalScheduler.runDelayed(
                plugin,
                t -> task.run(), delay
        );
    }

    @Override
    public void runLaterAsync(@NotNull Runnable task, long delay) {
        asyncScheduler.runDelayed(
                plugin,
                t -> task.run(),
                delay * 50L,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public @NotNull WrappedTask<?> runRepeating(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(
                globalScheduler.runAtFixedRate(
                        plugin,
                        t -> task.run(),
                        delay,
                        period
                )
        );
    }

    @Override
    public @NotNull WrappedTask<?> runRepeatingAsync(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(
                asyncScheduler.runAtFixedRate(
                        plugin,
                        t -> task.run(),
                        delay * 50L,
                        period * 50L,
                        TimeUnit.MILLISECONDS
                )
        );
    }
}
