package dev.enco.greatcombat.core.scoreboard.impl;

import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import fr.mrmicky.fastboard.FastBoard;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.List;
import java.util.UUID;

public class FastBoardProvider implements ScoreboardProvider {
    private final Reference2ObjectMap<UUID, FastBoard> boards = new Reference2ObjectOpenHashMap<>();

    @Override
    public void setScoreboard(IUser user, String title, List<String> lines) {
        var uuid = user.getPlayerUUID();
        var board = boards.get(uuid);
        if (board == null) {
            board = new FastBoard(user.asPlayer());
            boards.put(uuid, board);
        }
        board.updateTitle(title);
        board.updateLines(lines);
    }

    @Override
    public void resetScoreboard(IUser user) {
        var uuid = user.getPlayerUUID();
        var board = boards.get(uuid);
        if (board != null) {
            board.delete();
            boards.remove(uuid);
        }
    }
}
