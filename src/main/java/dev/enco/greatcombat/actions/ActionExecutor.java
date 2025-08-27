package dev.enco.greatcombat.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.context.Context;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Utility class for executing actions on players.
 * Processes action maps and executes all actions with their respective contexts.
 */
@UtilityClass
public class ActionExecutor {
    /**
     * Executes all actions in the provided action map for the specified player.
     *
     * @param player The player to execute actions on
     * @param actions Map of ActionType to list of Contexts to execute
     * @param replacement Optional replacement values for context formatting
     */
    public void execute(Player player, ImmutableMap<ActionType, List<Context>> actions, String... replacement) {
        for (var type : actions.keySet()) {
            var contexts = actions.get(type);
            var action = type.getAction();
            for (Context context : contexts) {
                action.execute(player, context, replacement);
            }
        }
    }
}



