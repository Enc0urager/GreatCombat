package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.ItemUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CopySubcommand implements Subcommand {
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player pl) {
            var item = pl.getInventory().getItemInMainHand();
            final Locale locale = configManager.getLocale();
            if (item == null) {
                pl.sendMessage(locale.emptyItem());
                return true;
            }
            Component component = Component.text(locale.click2Copy()).clickEvent(ClickEvent.copyToClipboard(ItemUtils.encode(item)));
            pl.sendMessage(component);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return List.of();
    }
}
