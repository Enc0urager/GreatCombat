package dev.enco.greatcombat.restrictions.prevention;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.utils.EnumUtils;
import dev.enco.greatcombat.utils.ItemSerializer;
import dev.enco.greatcombat.utils.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PreventionManager {
    private final List<PreventableItem> preventableItems = new ArrayList<>();

    public PreventableItem getPreventableItem(ItemStack itemStack) {
        return preventableItems.stream()
                .filter(item -> MetaManager.isSimilar(item.itemStack(), itemStack, item.checkedMetas()))
                .findFirst()
                .orElse(null);
    }

    public void load(ConfigurationSection section) {
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = EnumUtils.toEnumSet(
                    itemSection.getStringList("handlers"),
                    InteractionHandler.class,
                    handler -> Logger.warn("Обработчик " + handler + " не существует")
            );

            var metas = EnumUtils.toEnumSet(
                    itemSection.getStringList("checked-meta"),
                    CheckedMeta.class,
                    meta -> Logger.warn("У предметов нельзя проверять " + meta)
            );

            var types = EnumUtils.toEnumSet(
                    itemSection.getStringList("types"),
                    PreventionType.class,
                    type -> Logger.warn("Тип блокировки " + type + " не существует")
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
