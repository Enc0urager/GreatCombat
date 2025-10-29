package dev.enco.greatcombat.scoreboard.impl;

import com.xism4.sternalboard.SternalBoardHandler;
import dev.enco.greatcombat.manager.User;
import dev.enco.greatcombat.scoreboard.ScoreboardProvider;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.List;
import java.util.UUID;

public class SternalBoardProvider implements ScoreboardProvider {
    private final Reference2ObjectMap<UUID, SternalBoardHandler> handlers = new Reference2ObjectOpenHashMap<>();

    @Override
    public void setScoreboard(User user, String title, List<String> lines) {
        var uuid = user.getPlayerUUID();
        SternalBoardHandler handler = handlers.get(uuid);
        if (handler == null) {
            handler = new SternalBoardHandler(user.toPlayer());
            handlers.put(uuid, handler);
        }
        handler.updateTitle(title);
        handler.updateLines(lines);
    }

    @Override
    public void resetScoreboard(User user) {
        var uuid = user.getPlayerUUID();
        var handler = handlers.get(uuid);
        if (handler != null) {
            handler.delete();
            handlers.remove(uuid);
        }
    }
}
