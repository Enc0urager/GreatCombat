package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.Context;
import dev.enco.greatcombat.actions.context.SoundContext;
import dev.enco.greatcombat.actions.context.StringContext;
import dev.enco.greatcombat.actions.context.TitleContext;
import dev.enco.greatcombat.actions.impl.*;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum ActionType {
    ACTIONBAR(ActionBarAction::new, StringContext.class),
    BROADCASTACTIONBAR(BroadcastActionBarAction::new, StringContext.class),
    BROADCASTMESSAGE(BroadcastMessageAction::new, StringContext.class),
    BROADCASTSOUND(BroadcastSoundAction::new, SoundContext.class),
    BROADCASTTITLE(BroadcastTitleAction::new, TitleContext.class),
    CONSOLE(ConsoleAction::new, StringContext.class),
    MESSAGE(MessageAction::new, StringContext.class),
    PLAYER(PlayerAction::new, StringContext.class),
    SOUND(SoundAction::new, SoundContext.class),
    TITLE(TitleAction::new, TitleContext.class);

    private final Supplier<Action<?>> actionSupplier;
    private final Class<? extends Context> contextType;

    ActionType(Supplier<Action<?>> actionSupplier, Class<? extends Context> contextType) {
        this.actionSupplier = actionSupplier;
        this.contextType = contextType;
    }

    @SuppressWarnings("unchecked")
    public <C extends Context> Action<C> getAction() {
        return (Action<C>) actionSupplier.get();
    }
}


