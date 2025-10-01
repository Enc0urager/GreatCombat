package dev.enco.greatcombat.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.context.*;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Utility class for parsing and validating action configurations.
 * Transforms string-based action configurations into executable action maps.
 */
@UtilityClass
public class ActionRegistry {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, Function<String, ? extends Context>> VALIDATORS = ImmutableMap.of(
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
    public ImmutableMap<ActionType, ImmutableList<Context>> transform(List<String> settings) {
        Locale locale = ConfigManager.getLocale();
        Map<ActionType, ImmutableList.Builder<Context>> actions = new HashMap<>();
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
            ImmutableList.Builder<Context> contextBuilder = actions.computeIfAbsent(
                    type, k -> ImmutableList.builder()
            );

            Function<String, ? extends Context> validator = VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr);
            if (context != null) {
                contextBuilder.add(context);
            }
        }
        ImmutableMap.Builder<ActionType, ImmutableList<Context>> result = ImmutableMap.builder();

        for (var entry : actions.entrySet())
            result.put(entry.getKey(), entry.getValue().build());

        return result.build();
    }
}



