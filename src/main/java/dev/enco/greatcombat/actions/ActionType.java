package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.Context;
import dev.enco.greatcombat.actions.context.SoundContext;
import dev.enco.greatcombat.actions.context.StringContext;
import dev.enco.greatcombat.actions.context.TitleContext;
import dev.enco.greatcombat.actions.impl.*;
import lombok.Getter;

@Getter
public enum ActionType {
    ACTIONBAR(new ActionBarAction(), StringContext.class),
    BROADCASTACTIONBAR(new BroadcastActionBarAction(), StringContext.class),
    BROADCASTMESSAGE(new BroadcastMessageAction(), StringContext.class),
    BROADCASTSOUND(new BroadcastSoundAction(), SoundContext.class),
    BROADCASTTITLE(new BroadcastTitleAction(), TitleContext.class),
    CONSOLE(new ConsoleAction(), StringContext.class),
    MESSAGE(new MessageAction(), StringContext.class),
    PLAYER(new PlayerAction(), StringContext.class),
    SOUND(new SoundAction(), SoundContext.class),
    TITLE(new TitleAction(), TitleContext.class);

    private final Action<?> action;
    private final Class<? extends Context> contextType;

    ActionType(Action<?> action, Class<? extends Context> contextType) {
        this.action = action;
        this.contextType = contextType;
    }

    public <C extends Context> Action<C> getAction() {
        return (Action<C>) action;
    }
}


