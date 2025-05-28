package dev.enco.greatcombat.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import java.util.Base64;

@UtilityClass
public class ItemSerializer {
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    public String encode(ItemStack item) {
        return encoder.encodeToString(item.serializeAsBytes());
    }

    public ItemStack decode(String s) {
        return ItemStack.deserializeBytes(decoder.decode(s));
    }
}
