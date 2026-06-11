package dev.enco.greatcombat.api.models;

import org.jetbrains.annotations.NotNull;

/**
 * Unified task scheduler interface
 *
 * @see Runnable
 * @see WrappedTask
 */
public interface IScheduler {
    /** Runs task synchronously
     *
     * @param task Runnable to execute
     */
    void run(@NotNull Runnable task);

    /** Runs task asynchronously
     *
     * @param task Runnable to execute
     */
    void runAsync(@NotNull Runnable task);

    /**
     * Runs task later synchronously
     *
     * @param task  Runnable to execute
     * @param delay Delay in ticks before execution
     */
    void runLater(@NotNull Runnable task,
                  long delay);

    /**
     * Runs task later asynchronously
     *
     * @param task  Runnable to execute
     * @param delay Delay in ticks before execution
     */
    void runLaterAsync(@NotNull Runnable task,
                       long delay);

    /**
     * Runs repeating task synchronously
     *
     * @param task   Runnable to execute
     * @param delay  Delay in ticks before execution
     * @param period Period between executions
     * @return WrappedTask for cancellation control
     */
    @NotNull WrappedTask<?> runRepeating(@NotNull Runnable task,
                                         long delay,
                                         long period);

    /**
     * Runs repeating task asynchronously
     *
     * @param task   Runnable to execute
     * @param delay  Delay in ticks before execution
     * @param period Period between executions
     * @return WrappedTask for cancellation control
     */
    @NotNull WrappedTask<?> runRepeatingAsync(@NotNull Runnable task,
                                              long delay,
                                              long period);
}
