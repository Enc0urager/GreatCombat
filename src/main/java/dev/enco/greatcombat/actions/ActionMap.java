package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.Context;
import org.bukkit.entity.Player;

public class ActionMap {
    public static final ActionMap EMPTY = new ActionMap(new ActionType[0], new Context[0][]);
    private final ActionType[] types;
    private final Context[][] contexts;
    private final int size;

    public ActionMap(ActionType[] types, Context[][] contexts) {
        this.types = types;
        this.contexts = contexts;
        this.size = types.length;
    }

    public void execute(Player player, String... replacements) {
        for (int i = 0; i < size; i++) {
            ActionType type = types[i];
            Context[] typeContexts = contexts[i];
            var action = type.getAction();
            for (Context context : typeContexts)
                action.execute(player, context, replacements);
        }
    }
}
