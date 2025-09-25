package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.*;
import dev.enco.greatcombat.actions.impl.*;
import lombok.Getter;

/**
 * Enum representing different types of executable actions.
 * Each action type has an associated Action implementation and Context type.
 */
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
    TITLE(new TitleAction(), TitleContext.class),
    REMOVE_ITEMS(new RemoveItemAction(), MaterialContext.class),
    BACK_ITEMS(new BackItemsAction(), StringContext.class);

    private final Action<?> action;
    private final Class<? extends Context> contextType;

    ActionType(Action<?> action, Class<? extends Context> contextType) {
        this.action = action;
        this.contextType = contextType;
    }

    /**
     * Gets the typed action instance.
     *
     * @param <C> The context type
     * @return The typed action instance
     */
    public <C extends Context> Action<C> getAction() {
        return (Action<C>) action;
    }
}


