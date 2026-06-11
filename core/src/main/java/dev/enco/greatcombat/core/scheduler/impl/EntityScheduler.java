package dev.enco.greatcombat.core.scheduler.impl;

import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.scheduler.tasks.FoliaTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EntityScheduler implements IScheduler {
    private final JavaPlugin plugin;
    private final Player player;

    @Override
    public void run(@NotNull Runnable task) {
        player.getScheduler().run(
                plugin,
                t -> task.run(),
                null
        );
    }

    @Override
    public void runAsync(@NotNull Runnable task) {
        run(task);
    }

    @Override
    public void runLater(@NotNull Runnable task, long delay) {
        player.getScheduler().runDelayed(
                plugin,
                t -> task.run(),
                null,
                delay
        );
    }

    @Override
    public void runLaterAsync(@NotNull Runnable task, long delay) {
        runLater(task, delay);
    }

    @Override
    public @NotNull WrappedTask<?> runRepeating(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(
                player.getScheduler().runAtFixedRate(
                        plugin,
                        t -> task.run(),
                        null,
                        delay,
                        period
                )
        );
    }

    @Override
    public @NotNull WrappedTask<?> runRepeatingAsync(@NotNull Runnable task, long delay, long period) {
        return runRepeating(task, delay, period);
    }
}
