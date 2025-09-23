package dev.enco.greatcombat.scheduler.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.scheduler.IScheduler;
import dev.enco.greatcombat.scheduler.WrappedTask;
import dev.enco.greatcombat.scheduler.tasks.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


public class BukkitScheduler implements IScheduler {
    @Override
    public void run(Runnable task) {
        Bukkit.getScheduler().runTask(GreatCombat.getInstance(), task);
    }

    @Override
    public void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(GreatCombat.getInstance(), task);
    }

    @Override
    public void runLater(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(GreatCombat.getInstance(), task, delay);
    }

    @Override
    public void runLaterAsync(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GreatCombat.getInstance(), task, delay);
    }

    @Override
    public WrappedTask<?> runRepeating(Runnable task, long delay, long period) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };
        runnable.runTaskTimer(GreatCombat.getInstance(), delay, period);
        return new BukkitTask(runnable);
    }

    @Override
    public WrappedTask<?> runRepeatingAsync(Runnable task, long delay, long period) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };
        runnable.runTaskTimerAsynchronously(GreatCombat.getInstance(), delay, period);
        return new BukkitTask(runnable);
    }
}
