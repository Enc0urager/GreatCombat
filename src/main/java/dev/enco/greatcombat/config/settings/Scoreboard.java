package dev.enco.greatcombat.config.settings;

import java.util.List;

public record Scoreboard(
        String title,
        List<String> lines,
        String opponent,
        String empty,
        boolean enable
)
{}
