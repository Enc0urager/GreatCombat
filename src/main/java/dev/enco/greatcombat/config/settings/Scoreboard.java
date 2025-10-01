package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableList;

public record Scoreboard(
        String title,
        ImmutableList<String> lines,
        String opponent,
        String empty,
        boolean enable
)
{}
