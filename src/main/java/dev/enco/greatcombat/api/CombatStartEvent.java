package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CombatStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final User damager;
    private final User target;
    private boolean cancelled;
    private final Cancellable e;

    public CombatStartEvent(User damager, User target, Cancellable e) {
        this.damager = damager;
        this.target = target;
        this.e = e;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
        if (e != null) e.setCancelled(true);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
