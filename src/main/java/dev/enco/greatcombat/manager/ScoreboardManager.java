package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.Placeholders;
import lombok.experimental.UtilityClass;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import java.util.*;

@UtilityClass
public class ScoreboardManager {
    private final dev.enco.greatcombat.config.settings.Scoreboard boardSettings = ConfigManager.getScoreboard();
    private final TabAPI tabAPI = TabAPI.getInstance();
    private final me.neznamy.tab.api.scoreboard.ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();

    public void setScoreboard(User user, String time) {
        if (boardSettings.enable()) {
            List<String> replaced = new ArrayList<>();
            for (var line : boardSettings.lines()) {
                if (line.equals("{opponents}")) replaced.addAll(getOpponents(user));
                else replaced.add(line.replace("{time}", time));
            }
            Scoreboard sb = scoreboardManager.createScoreboard(
                    "greatcombat",
                    boardSettings.title(),
                    replaced
            );
            scoreboardManager.showScoreboard(tabAPI.getPlayer(user.getPlayerUUID()), sb);
        }
    }

    public void resetScoreboard(User user) {
        if (boardSettings.enable()) {
            scoreboardManager.resetScoreboard(tabAPI.getPlayer(user.getPlayerUUID()));
        }
    }

    private List<String> getOpponents(User user) {
        List<String> opponentsList = new ArrayList<>();
        var opponents = user.getOpponents();
        if (opponents.isEmpty()) opponentsList.add(boardSettings.empty());
        for (var opponent : opponents) {
            var player = opponent.toPlayer();
            opponentsList.add(Placeholders.replaceInBoard(player, boardSettings.opponent()));
        }
        return opponentsList;
    }
}
