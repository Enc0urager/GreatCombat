package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IRegionListener;

/**
 * Manager responsible for handling region listeners registration
 */
public interface IRegionManager extends IManager {
    /**
     * Sets the active region listener.
     * <p>
     * Automatically unregisters the previously registered listener (if present)
     * before registering the new one.
     *
     * @param listener the {@link IRegionListener} to register
     * @throws IllegalArgumentException if listener is null
     */
    void setListener(IRegionListener listener);

    /**
     * Returns the currently registered region listener.
     *
     * @return the currently active {@link IRegionListener}, or null if none is set
     */
    IRegionListener getListener();
}
