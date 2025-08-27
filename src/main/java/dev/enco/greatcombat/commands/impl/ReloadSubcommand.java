package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadSubcommand implements Subcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        GreatCombat.getInstance().getConfigManager().reload();
        sender.sendMessage(ConfigManager.getLocale().reload());
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return List.of();
    }
}
