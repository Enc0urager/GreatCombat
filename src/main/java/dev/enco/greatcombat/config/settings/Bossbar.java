package dev.enco.greatcombat.config.settings;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public record Bossbar(
        BarStyle style,
        BarColor color,
        String title,
        boolean progress,
        long updInterval,
        boolean enable
) {}
