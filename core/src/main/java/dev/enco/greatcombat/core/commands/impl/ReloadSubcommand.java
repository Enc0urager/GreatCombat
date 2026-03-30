package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ReloadSubcommand implements Subcommand {
    private final ConfigManager configManager;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        configManager.reload();
        sender.sendMessage(configManager.getLocale().reload());
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return List.of();
    }
}
