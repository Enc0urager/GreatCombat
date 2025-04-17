package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerLeaveInCombatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final User user;

    public PlayerLeaveInCombatEvent(User user) {
        this.user = user;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
