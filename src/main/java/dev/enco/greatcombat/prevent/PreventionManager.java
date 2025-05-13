package dev.enco.greatcombat.prevent;

import dev.enco.greatcombat.cooldowns.InteractionHandler;
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
                    .filter(item -> item.itemStack().isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }

    public void load(ConfigurationSection section) {
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = new ArrayList<InteractionHandler>();
            for (var handler : itemSection.getStringList("handlers")) try {
                handlers.add(InteractionHandler.valueOf(handler));
            } catch (IllegalArgumentException e) {
                Logger.warn("Обработчик " + handler + " не существует");
            }

            var types = new ArrayList<PreventionType>();
            for (var type : itemSection.getStringList("types")) try {
                types.add(PreventionType.valueOf(type));
            } catch (IllegalArgumentException e) {
                Logger.warn("Тип блокировки " + type + " не существует");
            }

            preventableItems.add(new PreventableItem(
                    ItemSerializer.decode(itemSection.getString("base64")),
                    itemSection.getString("translation"),
                    types,
                    handlers

            ));
        }
    }
}
