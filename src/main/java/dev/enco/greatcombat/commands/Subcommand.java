package dev.enco.greatcombat.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Subcommand {
    boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args);
    @Nullable List<String> onTab();
}
