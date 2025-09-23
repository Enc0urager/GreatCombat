package dev.enco.greatcombat.scheduler.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.scheduler.IScheduler;
import dev.enco.greatcombat.scheduler.WrappedTask;
import dev.enco.greatcombat.scheduler.tasks.FoliaTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class EntityScheduler implements IScheduler {
    private final Player player;

    @Override
    public void run(Runnable task) {
        player.getScheduler().run(
                GreatCombat.getInstance(),
                t -> task.run(),
                null
        );
    }

    @Override
    public void runAsync(Runnable task) {
        run(task);
    }

    @Override
    public void runLater(Runnable task, long delay) {
        player.getScheduler().runDelayed(
                GreatCombat.getInstance(),
                t -> task.run(),
                null,
                delay
        );
    }

    @Override
    public void runLaterAsync(Runnable task, long delay) {
        runLater(task, delay);
    }

    @Override
    public WrappedTask<?> runRepeating(Runnable task, long delay, long period) {
        return new FoliaTask(
                player.getScheduler().runAtFixedRate(
                        GreatCombat.getInstance(),
                        t -> task.run(),
                        null,
                        delay,
                        period
                )
        );
    }

    @Override
    public WrappedTask<?> runRepeatingAsync(Runnable task, long delay, long period) {
        return runRepeating(task, delay, period);
    }
}
