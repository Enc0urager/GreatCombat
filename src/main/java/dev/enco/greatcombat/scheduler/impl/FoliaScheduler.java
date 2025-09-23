package dev.enco.greatcombat.scheduler.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.scheduler.IScheduler;
import dev.enco.greatcombat.scheduler.WrappedTask;
import dev.enco.greatcombat.scheduler.tasks.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements IScheduler {
    private final GlobalRegionScheduler globalScheduler = GreatCombat.getInstance().getServer().getGlobalRegionScheduler();
    private final AsyncScheduler asyncScheduler = GreatCombat.getInstance().getServer().getAsyncScheduler();

    @Override
    public void run(Runnable task) {
        globalScheduler.run(
                GreatCombat.getInstance(),
                t -> task.run()
        );
    }

    @Override
    public void runAsync(Runnable task) {
        asyncScheduler.runNow(
                GreatCombat.getInstance(),
                t -> task.run()
        );
    }

    @Override
    public void runLater(Runnable task, long delay) {
        globalScheduler.runDelayed(
                GreatCombat.getInstance(),
                t -> task.run(), delay
        );
    }

    @Override
    public void runLaterAsync(Runnable task, long delay) {
        asyncScheduler.runDelayed(
                GreatCombat.getInstance(),
                t -> task.run(),
                delay * 50L,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public WrappedTask<?> runRepeating(Runnable task, long delay, long period) {
        return new FoliaTask(
                globalScheduler.runAtFixedRate(
                        GreatCombat.getInstance(),
                        t -> task.run(),
                        delay,
                        period
                )
        );
    }

    @Override
    public WrappedTask<?> runRepeatingAsync(Runnable task, long delay, long period) {
        return new FoliaTask(
                asyncScheduler.runAtFixedRate(
                        GreatCombat.getInstance(),
                        t -> task.run(),
                        delay * 50L,
                        period * 50L,
                        TimeUnit.MILLISECONDS
                )
        );
    }
}
