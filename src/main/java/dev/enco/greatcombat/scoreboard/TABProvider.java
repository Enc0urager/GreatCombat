package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.manager.User;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import java.util.List;

public class TABProvider implements ScoreboardProvider {
    private final TabAPI tabAPI = TabAPI.getInstance();
    private final me.neznamy.tab.api.scoreboard.ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();

    @Override
    public void setScoreboard(User user, String title, List<String> lines) {
        Scoreboard sb = scoreboardManager.createScoreboard(
                "greatcombat",
                title,
                lines
        );
        scoreboardManager.showScoreboard(tabAPI.getPlayer(user.getPlayerUUID()), sb);
    }

    @Override
    public void resetScoreboard(User user) {
        scoreboardManager.resetScoreboard(tabAPI.getPlayer(user.getPlayerUUID()));
    }
}
