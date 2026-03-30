package dev.enco.greatcombat.core.regions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RegionManager implements IRegionManager {
    private final JavaPlugin plugin;
    private IRegionListener listener;

    @Override
    public void setListener(IRegionListener listener) {
        if (listener == null) throw new IllegalArgumentException("IRegionListener cannot be null");
        if (this.listener != null) this.listener.unregisterListener();
        this.listener = listener;
        listener.registerListener(plugin);
    }

    @Override
    public IRegionListener getListener() {
        return listener;
    }
}
