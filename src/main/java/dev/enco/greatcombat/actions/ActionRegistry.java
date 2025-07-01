package dev.enco.greatcombat.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.context.Context;
import dev.enco.greatcombat.actions.context.SoundContext;
import dev.enco.greatcombat.actions.context.StringContext;
import dev.enco.greatcombat.actions.context.TitleContext;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, Function<String, ? extends Context>> VALIDATORS = ImmutableMap.of(
            StringContext.class, StringContext::validate,
            TitleContext.class, TitleContext::validate,
            SoundContext.class, SoundContext::validate
    );

    public ImmutableMap<ActionType, List<Context>> transform(List<String> settings) {
        Locale locale = ConfigManager.getLocale();
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
            actions.putIfAbsent(type, new ArrayList<>());

            Function<String, ? extends Context> validator = VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr);
            if (context != null) {
                actions.get(type).add(context);
            }
        }
        return ImmutableMap.copyOf(actions);
    }
}



