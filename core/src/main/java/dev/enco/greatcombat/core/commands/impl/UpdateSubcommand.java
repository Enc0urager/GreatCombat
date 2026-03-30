package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpdateSubcommand implements Subcommand {
    private final ITaskManager taskManager;
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        taskManager.getGlobalScheduler().runAsync(() -> {
            UpdateUtils.check(configManager.getLocale(), ver -> {
                String current = plugin.getDescription().getVersion();
                Locale locale = configManager.getLocale();
                if (ver.equals(current)) {
                    sender.sendMessage(locale.updatesNotFound());
                    return;
                }
                UpdateUtils.update(plugin, ver, sender);
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
