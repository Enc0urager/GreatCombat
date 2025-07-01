package dev.enco.greatcombat.utils.colorizer;

import dev.enco.greatcombat.utils.colorizer.impl.LegacyIColorizer;
import dev.enco.greatcombat.utils.colorizer.impl.MinimessageIColorizer;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Colorizer {
    private IColorizer colorizer;

    public void setColorizer(String type) {
        switch (type) {
            case "MINIMESSAGE" -> colorizer = new MinimessageIColorizer();
            default -> colorizer = new LegacyIColorizer();
        }
    }

    public String colorize(String message) {
        if (message == null || message.isEmpty()) return message;
        return colorizer.colorize(message);
    }

    public List<String> colorizeAll(List<String> list) {
        List<String> colored = new ArrayList<>();
        for (var str : list) colored.add(colorize(str));
        return colored;
    }
}
