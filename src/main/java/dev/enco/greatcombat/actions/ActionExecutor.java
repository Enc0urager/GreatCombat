package dev.enco.greatcombat.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.context.Context;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class ActionExecutor {
    public void execute(Player player, ImmutableMap<ActionType, List<Context>> actions, String first, String second) {
        for (var type : actions.keySet()) {
            var contexts = actions.get(type);
            var action = type.getAction();
            for (Context context : contexts) {
                action.execute(player, context);
            }
        }
    }
}



