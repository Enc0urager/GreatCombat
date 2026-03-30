package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IRegionListener;

public interface IRegionManager extends IManager {
    void setListener(IRegionListener listener);
    IRegionListener getListener();
}
