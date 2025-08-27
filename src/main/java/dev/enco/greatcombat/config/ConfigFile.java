package dev.enco.greatcombat.config;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Getter
public class ConfigFile {
    private final GreatCombat plugin = GreatCombat.getInstance();
    private FileConfiguration fileConfiguration;
    private File file;
    private final String name;
    private final File folder;
    private final String filePath;

    public ConfigFile(String name, File folder, String lang, boolean replace) {
        this.folder = folder;
        this.name = name;
        filePath = "resources-" + lang + "/" + name + ".yml";
        this.file = new File(folder, name + ".yml");
        if (replace || !this.file.exists()) {
            try {
                try (InputStream is = plugin.getResource(filePath)) {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                Logger.error("Unable to create config file " + name + ".yml: " + e.getMessage());
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {
        if (this.fileConfiguration == null) {
            this.file = new File(this.folder, this.name + ".yml");
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        var inputStreamReader = new InputStreamReader(plugin.getResource(filePath), StandardCharsets.UTF_8);
        var configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
        this.fileConfiguration.setDefaults(configuration);
    }

    public FileConfiguration get() {
        return this.fileConfiguration;
    }
}

