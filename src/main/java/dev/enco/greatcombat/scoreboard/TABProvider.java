package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.manager.User;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;

import java.util.List;

public class TABProvider implements ScoreboardProvider {

    @Override
    public void setScoreboard(User user, String title, List<String> lines) {
        TabAPI tabAPI = TabAPI.getInstance();
        ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();
        Scoreboard sb = scoreboardManager.createScoreboard(
                "greatcombat",
                title,
                lines
        );
        scoreboardManager.showScoreboard(tabAPI.getPlayer(user.getPlayerUUID()), sb);
    }

    @Override
    public void resetScoreboard(User user) {
        TabAPI tabAPI = TabAPI.getInstance();
        ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();
        var uuid = user.getPlayerUUID();
        scoreboardManager.resetScoreboard(tabAPI.getPlayer(uuid));
    }
}
