package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleAction implements Action {
    @Override
    public void execute(@NotNull Player player, String context) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), context);
    }
}
