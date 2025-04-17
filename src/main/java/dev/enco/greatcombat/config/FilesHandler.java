package dev.enco.greatcombat.config;

import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FilesHandler {
    private final List<ConfigFile> configFiles = new ArrayList();

    public void addConfigFile2List(ConfigFile configFile) {
        configFiles.add(configFile);
    }

    public void reloadAll() {
        configFiles.forEach((configFile) -> {
            configFile.reload();
        });
    }

    public ConfigFile getConfigFile(String name) {
        return configFiles.stream()
                .filter(configFile -> configFile.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(name));
    }
}
