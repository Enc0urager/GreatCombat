package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerKickInCombatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final User user;
    private final String reason;

    public PlayerKickInCombatEvent(User user, String reason) {
        this.user = user;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
