package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.scheduler.TaskManager;
import dev.enco.greatcombat.utils.UpdateUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpdateSubcommand implements Subcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        TaskManager.getGlobalScheduler().runAsync(() -> {
            UpdateUtils.check(ver -> {
                String current = GreatCombat.getInstance().getDescription().getVersion();
                Locale locale = ConfigManager.getLocale();
                if (ver.equals(current)) {
                    sender.sendMessage(locale.updatesNotFound());
                    return;
                }
                UpdateUtils.update(ver, sender);
                sender.sendMessage(locale.updated());
            });
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return List.of();
    }
}
