package dev.enco.greatcombat.core.scoreboard.impl;

import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;

import java.util.List;

public class TABProvider implements ScoreboardProvider {
    @Override
    public void setScoreboard(IUser user, String title, List<String> lines) {
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
    public void resetScoreboard(IUser user) {
        TabAPI tabAPI = TabAPI.getInstance();
        ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();
        var uuid = user.getPlayerUUID();
        scoreboardManager.resetScoreboard(tabAPI.getPlayer(uuid));
    }
}
