package dev.enco.greatcombat.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.utils.Logger;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");

    public ImmutableMap<ActionType, List<String>> transform(List<String> settings) {
        Map<ActionType, List<String>> actions = new HashMap<>();
        for (var s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn("Illegal action pattern " + s);
                continue;
            }
            var type = ActionType.valueOf(matcher.group(1));
            if (type == null) {
                Logger.warn("ActionType " + s + " is not available!");
                continue;
            }
            var context = matcher.group(2).trim();
            actions.putIfAbsent(type, new ArrayList<>());
            actions.get(type).add(Colorizer.colorize(context));
        }
        return ImmutableMap.copyOf(actions);
    }
}



