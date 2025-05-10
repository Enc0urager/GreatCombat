package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.manager.User;
import fr.mrmicky.fastboard.FastBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FastBoardProvider implements ScoreboardProvider {
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    @Override
    public void setScoreboard(User user, String title, List<String> lines) {
        var uuid = user.getPlayerUUID();
        var board = boards.get(uuid);
        if (board == null) {
            board = new FastBoard(user.toPlayer());
            boards.put(uuid, board);
        }
        board.updateTitle(title);
        board.updateLines(lines);
    }

    @Override
    public void resetScoreboard(User user) {
        var uuid = user.getPlayerUUID();
        var board = boards.get(uuid);
        if (board != null) {
            board.delete();
            boards.remove(uuid);
        }
    }
}
