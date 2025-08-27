package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CombatStartEvent extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();

    public CombatStartEvent(User damager, User target) {
        super(damager, target);
    }

    public CombatStartEvent(User damager, User target, EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
