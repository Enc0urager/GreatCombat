package dev.enco.greatcombat.utils.colorizer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MinimessageColorizer implements ColorizerType {
    @Override
    public String colorize(String message) {
        var comp = MiniMessage.miniMessage().deserialize(message);
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }
}
