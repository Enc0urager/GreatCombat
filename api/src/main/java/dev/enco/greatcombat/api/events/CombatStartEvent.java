package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class CombatStartEvent extends CombatDamageEvent {
    private static final @NotNull HandlerList handlers = new HandlerList();

    public CombatStartEvent(@NotNull IUser damager,
                            @NotNull IUser target
    ) {
        super(damager, target);
    }

    public CombatStartEvent(@NotNull IUser damager,
                            @NotNull IUser target,
                            @Nullable EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
