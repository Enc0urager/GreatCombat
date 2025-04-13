package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.impl.*;
import lombok.Getter;

@Getter
public enum ActionType {
    MESSAGE(new MessageAction()),
    SOUND(new SoundAction()),
    ACTIONBAR(new ActionBarAction()),
    TITLE(new TitleAction()),
    CONSOLE(new ConsoleAction()),
    PLAYER(new PlayerAction()),
    BROADCASTMESSAGE(new BroadcastMessageAction()),
    BROADCASTSOUND(new BroadcastSoundAction()),
    BROADCASTTITLE(new BroadcastTitleAction()),
    BROADCASTACTIONBAR(new BroadcastActionBarAction());

    private final Action action;

    ActionType(Action action) {
        this.action = action;
    }
}


