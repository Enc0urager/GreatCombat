package dev.enco.greatcombat.api;

/**
 * Static provider for accessing the {@link GreatCombatPlugin} instance.
 */
public final class GreatCombatProvider {
    private static GreatCombatPlugin plugin = null;

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws UnsupportedOperationException always, since this is a utility class
     */
    private GreatCombatProvider() {
        throw new UnsupportedOperationException("GreatCombatProvider cannot be initialized!");
    }

    /**
     * Checks whether the plugin instance has been initialized.
     *
     * @return true if the plugin is loaded, false otherwise
     */
    public static boolean isLoaded() {
        return plugin != null;
    }

    /**
     * Returns the active {@link GreatCombatPlugin} instance.
     *
     * @return the initialized plugin instance
     * @throws IllegalStateException if the plugin has not been initialized yet
     */
    public static GreatCombatPlugin getPlugin() {
        if (!isLoaded()) throw new IllegalStateException("GreatCombat isn't loaded yet!");
        return plugin;
    }

    /**
     * Sets the plugin instance.
     *
     * @param pl the plugin implementation to register
     * @throws IllegalStateException if the plugin has already been initialized
     */
    public static void setPlugin(GreatCombatPlugin pl) {
        if (isLoaded()) throw new IllegalStateException("GreatCombat is already loaded!");
        plugin = pl;
    }
}
