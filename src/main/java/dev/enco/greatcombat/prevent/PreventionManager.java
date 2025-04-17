package dev.enco.greatcombat.prevent;

import dev.enco.greatcombat.cooldowns.InteractionHandler;
import dev.enco.greatcombat.utils.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class PreventionManager {
    private final List<PreventableItem> preventableItems = new ArrayList<>();

    public PreventableItem getPreventableItem(Material material) {
        return preventableItems.stream()
                .filter(item -> item.material().equals(material))
                .findFirst()
                .orElse(null);
    }

    public void load(ConfigurationSection section) {
        for (var item : section.getKeys(false)) {
            Material material;
            try {
                material = Material.valueOf(item);
            } catch (IllegalArgumentException e) {
                Logger.warn("Material " + item + " is not available");
                continue;
            }
            var parts = section.getString(item).split(";");
            if (parts.length < 3) {
                Logger.warn("Write prevention item in format: MATERIAL: translation;Item type;handlers");
                continue;
            }
            List<PreventionType> preventionTypes = new ArrayList<>();
            var types = parts[1].split(",");
            for (var type :  Arrays.stream(types).toList()) {
                try {
                    preventionTypes.add(PreventionType.valueOf(type));
                } catch (IllegalArgumentException e) {
                    Logger.warn("Prevention type " + type + " is not available");
                }
            }
            List<InteractionHandler> preventionHandlers = new ArrayList<>();
            var handlers = parts[2].split(",");
            for (var handler :  Arrays.stream(handlers).toList()) {
                try {
                    preventionHandlers.add(InteractionHandler.valueOf(handler));
                } catch (IllegalArgumentException e) {
                    Logger.warn("Handler " + handler + " is not available");
                }
            }
            preventableItems.add(new PreventableItem(
                    material,
                    parts[0],
                    preventionTypes,
                    preventionHandlers

            ));
        }
    }
}
