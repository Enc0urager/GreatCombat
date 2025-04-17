package dev.enco.greatcombat.utils.colorizer;

import java.util.List;

public interface ColorizerType {
    String colorize(String message);
    List<String> colorizeAll(List<String> list);
}
