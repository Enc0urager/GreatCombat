package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

/**
 * Utility class for serializing and deserializing ItemStacks to/from Base64 strings.
 * Used for storing ItemStack in configuration files.
 */
@UtilityClass
public class ItemSerializer {
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Encodes an ItemStack to a Base64 string.
     *
     * @param item The ItemStack to encode
     * @return Base64 encoded string representation of the ItemStack
     */
    public String encode(ItemStack item) {
        return encoder.encodeToString(item.serializeAsBytes());
    }

    /**
     * Decodes a Base64 string back to an ItemStack.
     *
     * @param s The Base64 string to decode
     * @return The decoded ItemStack, or null if decoding fails
     */
    public ItemStack decode(String s) {
        try {
            return ItemStack.deserializeBytes(decoder.decode(s));
        } catch (NoSuchMethodError e) {
            for (var st : ConfigManager.getLocale().outdatedCore()) Logger.error(st);
            GreatCombat.getInstance().getServer().getPluginManager().disablePlugin(GreatCombat.getInstance());
        }
        return null;
    }
}
