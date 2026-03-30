package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class StopSubcommand implements Subcommand {
    private final ConfigManager configManager;
    private final ICombatManager combatManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        final Locale locale = configManager.getLocale();
        if (args.length < 1) {
            sender.sendMessage(locale.notSpecifiedPlayer());
            return true;
        }
        var player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[0]));
            return true;
        }
        var user = combatManager.getUser(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(locale.playerNotInCombat());
            return true;
        }
        combatManager.stopCombat(user);
        sender.sendMessage(locale.stopSuccess());
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return null;
    }
}
