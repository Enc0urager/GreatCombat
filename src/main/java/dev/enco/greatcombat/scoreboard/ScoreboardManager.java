package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.Logger;
import dev.enco.greatcombat.utils.Placeholders;
import dev.enco.greatcombat.config.settings.Scoreboard;
import dev.enco.greatcombat.manager.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ScoreboardManager {
    private final Scoreboard boardSettings = ConfigManager.getScoreboard();
    private ScoreboardProvider provider;

    public void setProvider(String s) {
        switch (s) {
            case "TAB": {
                provider = new TABProvider();
                Logger.info("Используем TAB в качестве менеджера скорборда");
                return;
            }
            default: {
                provider = new FastBoardProvider();
                Logger.info("Используем FastBoard в качестве менеджера скорборда");
            }
        }
    }

    public void setScoreboard(User user, String time) {
        if (boardSettings.enable()) {
            provider.setScoreboard(user, boardSettings.title(), getLines(user, time));
        }
    }

    public void resetScoreboard(User user) {
        if (boardSettings.enable()) {
            provider.resetScoreboard(user);
        }
    }

    private List<String> getLines(User user, String time) {
        List<String> replaced = new ArrayList<>();
        for (var line : boardSettings.lines()) {
            if (line.equals("{opponents}")) replaced.addAll(getOpponents(user));
            else {
                replaced.add(Placeholders.replace(user.toPlayer(), line.replace("{time}", time)));
            }
        }
        return replaced;
    }

    private List<String> getOpponents(User user) {
        List<String> opponentsList = new ArrayList<>();
        var opponents = user.getOpponents();
        if (opponents.isEmpty()) opponentsList.add(boardSettings.empty());
        for (var opponent : opponents) {
            opponentsList.add(Placeholders.replaceInBoard(opponent.toPlayer(), boardSettings.opponent()));
        }
        return opponentsList;
    }
}
