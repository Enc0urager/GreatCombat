package dev.enco.greatcombat.restrictions.prevention;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.utils.EnumUtils;
import dev.enco.greatcombat.utils.ItemSerializer;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing item interaction prevention during combat.
 * Provides functionality to prevent specific item interactions based on configuration.
 */
@UtilityClass
public class PreventionManager {
    private final List<PreventableItem> preventableItems = new ArrayList<>();

    /**
     * Retrieves the PreventableItem associated with the given ItemStack.
     *
     * @param itemStack The ItemStack to check for prevention
     * @return PreventableItem if found, null otherwise
     */
    public PreventableItem getPreventableItem(ItemStack itemStack) {
        return preventableItems.stream()
                .filter(item -> MetaManager.isSimilar(item.itemStack(), itemStack, item.checkedMetas()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Loads preventable items from the configuration section.
     *
     * @param section The configuration section containing preventable item definitions
     */
    public void load(ConfigurationSection section) {
        final var locale = ConfigManager.getLocale();
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = EnumUtils.toEnumSet(
                    itemSection.getStringList("handlers"),
                    InteractionHandler.class,
                    handler -> Logger.warn(MessageFormat.format(locale.handlerDoesNotExist(), handler))
            );

            var metas = EnumUtils.toEnumSet(
                    itemSection.getStringList("checked-meta"),
                    CheckedMeta.class,
                    meta -> Logger.warn(MessageFormat.format(locale.metaDoesNotExist(), meta))
            );

            var types = EnumUtils.toEnumSet(
                    itemSection.getStringList("types"),
                    PreventionType.class,
                    type -> Logger.warn(MessageFormat.format(locale.blockerDoesNotExist(), type))
            );

            preventableItems.add(new PreventableItem(
                    ItemSerializer.decode(itemSection.getString("base64")),
                    itemSection.getString("translation"),
                    types,
                    handlers,
                    metas,
                    itemSection.getBoolean("set-material-cooldown")
            ));
        }
    }
}
