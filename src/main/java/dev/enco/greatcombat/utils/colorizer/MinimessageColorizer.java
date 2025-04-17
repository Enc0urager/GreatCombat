package dev.enco.greatcombat.utils.colorizer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class MinimessageColorizer implements ColorizerType {
    @Override
    public String colorize(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        var comp = MiniMessage.miniMessage().deserialize(message);
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }

    @Override
    public List<String> colorizeAll(List<String> list) {
        return List.of();
    }
}
