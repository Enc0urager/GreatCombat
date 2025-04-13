package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastTitleAction implements Action {
    @Override
    public void execute(@NotNull Player player, String context) {
        var action = ActionType.TITLE.getAction();
        for (var p : Bukkit.getOnlinePlayers()) {
            action.execute(p, context);
        }
    }
}
