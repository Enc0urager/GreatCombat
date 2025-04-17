package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CommandPreprocessInCombatEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final User user;
    private final String command;
    private boolean cancelled;
    private final Cancellable e;

    public CommandPreprocessInCombatEvent(User user, String command, Cancellable e) {
        this.user = user;
        this.command = command;
        this.e = e;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
        e.setCancelled(true);
    }
}
