package dev.enco.greatcombat.api;

import dev.enco.greatcombat.api.managers.IManager;

public interface GreatCombatPlugin {
    <T extends IManager> T getManager(Class<T> clazz);
}
