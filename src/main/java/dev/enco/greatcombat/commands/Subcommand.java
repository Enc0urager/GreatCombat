package dev.enco.greatcombat.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface Subcommand {
    boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args);
}
