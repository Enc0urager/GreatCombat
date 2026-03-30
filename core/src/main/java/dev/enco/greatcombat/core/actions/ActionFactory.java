package dev.enco.greatcombat.core.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.core.actions.context.*;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Utility class for parsing and validating action configurations.
 * Transforms string-based action configurations into executable action maps.
 */
@UtilityClass
public class ActionFactory {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, BiFunction<String, Locale, ? extends Context>> VALIDATORS = ImmutableMap.of(
            StringContext.class, StringContext::validate,
            TitleContext.class, TitleContext::validate,
            SoundContext.class, SoundContext::validate,
            MaterialContext.class, MaterialContext::validate
    );

    /**
     * Transforms a list of action configuration strings into an executable action map.
     * Parses action types and their contexts, validates them, and logs errors for invalid entries.
     *
     * @param settings List of action configuration strings in format "[ACTION_TYPE] context"
     * @return Immutable map of ActionType to list of validated Contexts
     */
    public ActionMap from(Locale locale, List<String> settings) {
        if (settings == null || settings.isEmpty()) return ActionMap.EMPTY;
        Map<ActionType, List<Context>> actions = new HashMap<>();
        for (var s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn(locale.illegalActionPattern() + s);
                continue;
            }
            var typeName = matcher.group(1);
            ActionType type;
            try {
                type = ActionType.valueOf(typeName);
            } catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(locale.actionDoesNotExist(), typeName));
                continue;
            }

            var contextStr = matcher.group(2).trim();
            List<Context> contexts = actions.computeIfAbsent(type, k -> new ArrayList<>());

            BiFunction<String, Locale, ? extends Context> validator =
                    VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr, locale);
            if (context != null) contexts.add(context);
        }

        ActionType[] types = new ActionType[actions.size()];
        Context[][] contexts = new Context[actions.size()][];

        int i = 0;
        for (var entry : actions.entrySet()) {
            types[i] = entry.getKey();
            List<Context> contextList = entry.getValue();
            contexts[i] = contextList.toArray(new Context[0]);
            i++;
        }

        return new ActionMap(types, contexts);
    }
}



