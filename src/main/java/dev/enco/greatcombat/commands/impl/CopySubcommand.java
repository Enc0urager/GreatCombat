package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.ItemSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CopySubcommand implements Subcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player pl) {
            if (!pl.hasPermission("greatcombat.admin")) return true;
            var item = pl.getInventory().getItemInMainHand();
            final Locale locale = ConfigManager.getLocale();
            if (item == null) {
                pl.sendMessage(locale.emptyItem());
                return true;
            }
            Component component = Component.text(locale.click2Copy()).clickEvent(ClickEvent.copyToClipboard(ItemSerializer.encode(item)));
            pl.sendMessage(component);
        }
        return true;
    }
}
