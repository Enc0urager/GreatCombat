package dev.enco.greatcombat.utils.colorizer;

import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class Colorizer {
    private ColorizerType colorizer;

    public void setColorizer(String type) {
        switch (type) {
            case "MINIMESSAGE" -> colorizer = new MinimessageColorizer();
            default -> colorizer = new LegacyColorizer();
        }
    }

    public String colorize(String message) {
        return colorizer.colorize(message);
    }

    public List<String> colorizeAll(List<String> list) {
        return colorizer.colorizeAll(list);
    }
}
