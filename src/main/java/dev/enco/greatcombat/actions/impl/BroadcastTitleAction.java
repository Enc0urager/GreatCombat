package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastTitleAction implements Action {
    @Override
    public void execute(@NotNull Player player, String context) {
        var args = context.split(";");
        var title = args.length > 0 ? args[0] : "";
        var subTitle = args.length > 1 ? args[1] : "";
        int fadeIn = args.length > 2 ? Integer.valueOf(args[2]) : 10;
        int stayIn = args.length > 3 ? Integer.valueOf(args[3]) : 70;
        int fadeOut = args.length > 4 ? Integer.valueOf(args[4]) : 20;
        for (var p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subTitle, fadeIn, stayIn, fadeOut);
        }
    }
}
