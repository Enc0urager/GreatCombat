package dev.enco.greatcombat.scheduler.tasks;

import dev.enco.greatcombat.scheduler.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FoliaTask implements WrappedTask<ScheduledTask> {
    private final ScheduledTask task;

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public ScheduledTask getRunnable() {
        return task;
    }
}
