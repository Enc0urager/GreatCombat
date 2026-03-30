package dev.enco.greatcombat.core.config.settings;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public record Bossbar(
        BarStyle style,
        BarColor color,
        String title,
        boolean progress,
        boolean enable
) {}
