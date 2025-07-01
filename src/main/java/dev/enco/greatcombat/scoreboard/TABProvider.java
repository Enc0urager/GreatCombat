package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.manager.User;
import dev.enco.greatcombat.utils.logger.Logger;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;

import java.util.List;

public class TABProvider implements ScoreboardProvider {
    private TabAPI tabAPI = TabAPI.getInstance();
    private ScoreboardManager scoreboardManager = tabAPI.getScoreboardManager();

    @Override
    public void setScoreboard(User user, String title, List<String> lines) {
        try {
            createAndShow(tabAPI, scoreboardManager, user, title, lines);
        } catch (IllegalStateException e) {
            changeInstance();
            createAndShow(tabAPI, scoreboardManager, user, title, lines);
        }
    }

    @Override
    public void resetScoreboard(User user) {
        var uuid = user.getPlayerUUID();
        try {
            scoreboardManager.resetScoreboard(tabAPI.getPlayer(uuid));
        } catch (IllegalStateException e) {
            changeInstance();
            scoreboardManager.resetScoreboard(tabAPI.getPlayer(uuid));
        }
    }

    private void changeInstance() {
        Logger.warn(ConfigManager.getLocale().tabDiscardedInstance());
        this.tabAPI = TabAPI.getInstance();
        this.scoreboardManager = tabAPI.getScoreboardManager();
    }

    private void createAndShow(TabAPI tabAPI, ScoreboardManager scoreboardManager, User user, String title, List<String> lines) {
        Scoreboard sb = scoreboardManager.createScoreboard(
                "greatcombat",
                title,
                lines
        );
        scoreboardManager.showScoreboard(tabAPI.getPlayer(user.getPlayerUUID()), sb);
    }
}
