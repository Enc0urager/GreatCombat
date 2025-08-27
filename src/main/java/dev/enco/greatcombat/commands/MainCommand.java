package dev.enco.greatcombat.commands;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("greatcombat.admin")) return true;
        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }
        try {
            var arg = CommandArg.valueOf(args[0].toUpperCase());
            arg.getSubcommand().onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        } catch (IllegalArgumentException e) {
            sendHelpMessage(sender);
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        final Locale locale = ConfigManager.getLocale();
        for (var s : locale.commandHelp()) sender.sendMessage(s);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("greatcombat.admin")) return null;
        if (args.length == 1) return
                Arrays.stream(CommandArg.values())
                        .map(arg -> arg.name().toLowerCase())
                        .toList();
        try {
            var arg = CommandArg.valueOf(args[0].toUpperCase());
            return arg.getSubcommand().onTab();
        } catch (IllegalArgumentException e) { return List.of(); }
    }
}
