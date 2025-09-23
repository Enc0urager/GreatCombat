package dev.enco.greatcombat.scheduler;

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
    void run(Runnable task);
    /** Runs task asynchronously
     *
     * @param task Runnable to execute
     */
    void runAsync(Runnable task);
    /**
     * Runs task later synchronously
     *
     * @param task Runnable to execute
     * @param delay Delay in ticks before execution
     */
    void runLater(Runnable task, long delay);
    /**
     * Runs task later asynchronously
     *
     * @param task Runnable to execute
     * @param delay Delay in ticks before execution
     */
    void runLaterAsync(Runnable task, long delay);
    /**
     * Runs repeating task synchronously
     *
     * @param task Runnable to execute
     * @param delay Delay in ticks before execution
     * @param period Period between executions
     * @return WrappedTask for cancellation control
     */
    WrappedTask<?> runRepeating(Runnable task, long delay, long period);
    /**
     * Runs repeating task asynchronously
     *
     * @param task Runnable to execute
     * @param delay Delay in ticks before execution
     * @param period Period between executions
     * @return WrappedTask for cancellation control
     */
    WrappedTask<?> runRepeatingAsync(Runnable task, long delay, long period);
}
