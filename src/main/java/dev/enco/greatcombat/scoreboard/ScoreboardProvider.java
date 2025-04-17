package dev.enco.greatcombat.scoreboard;

import dev.enco.greatcombat.manager.User;

import java.util.List;

public interface ScoreboardProvider {
    void setScoreboard(User user, String title, List<String> lines);
    void resetScoreboard(User user);
}
