package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.utils.Placeholders;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionExecutor {
    public void execute(Player player, Map<ActionType, List<String>> actions, String first, String second) {
        actions.keySet().forEach(type -> {
            var contexts = actions.get(type);
            for (String context : contexts) {
                var c = Placeholders.replaceInMessage(player, context, first, second);
                type.getAction().execute(player, c);
            }
        });
    }
}



