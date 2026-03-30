package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CombatStartEvent extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();

    public CombatStartEvent(IUser damager, IUser target) {
        super(damager, target);
    }

    public CombatStartEvent(IUser damager, IUser target, EntityDamageEvent.DamageCause cause) {
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
