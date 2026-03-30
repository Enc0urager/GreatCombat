package dev.enco.greatcombat.core.scheduler.tasks;

import dev.enco.greatcombat.api.models.WrappedTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class BukkitTask implements WrappedTask<BukkitRunnable> {
    private final BukkitRunnable runnable;

    @Override
    public void cancel() {
        runnable.cancel();
    }

    @Override
    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
