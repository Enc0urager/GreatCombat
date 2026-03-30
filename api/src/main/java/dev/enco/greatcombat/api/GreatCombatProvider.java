package dev.enco.greatcombat.api;

public final class GreatCombatProvider {
    private static GreatCombatPlugin plugin = null;

    private GreatCombatProvider() {
        throw new UnsupportedOperationException("GreatCombatProvider cannot be initialized!");
    }

    public static boolean isLoaded() {
        return plugin != null;
    }

    public static GreatCombatPlugin getPlugin() {
        if (!isLoaded()) throw new IllegalStateException("GreatCombat isn't loaded yet!");
        return plugin;
    }

    public static void setPlugin(GreatCombatPlugin pl) {
        if (isLoaded()) throw new IllegalStateException("GreatCombat is already loaded!");
        plugin = pl;
    }
}
