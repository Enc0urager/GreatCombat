package dev.enco.greatcombat.config;

import dev.enco.greatcombat.GreatCombat;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
public class ConfigFile {
    private final GreatCombat plugin = GreatCombat.getInstance();
    private FileConfiguration fileConfiguration;
    private File file;
    private final String name;
    private final File folder;

    public ConfigFile(String name, File folder) {
        this.folder = folder;
        this.name = name;
        this.file = new File(folder, name + ".yml");
        if (!this.file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {
        if (this.fileConfiguration == null) {
            this.file = new File(this.folder, this.name + ".yml");
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        var inputStreamReader = new InputStreamReader(plugin.getResource(this.name + ".yml"), StandardCharsets.UTF_8);
        var configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
        this.fileConfiguration.setDefaults(configuration);
    }

    public FileConfiguration get() {
        if (this.fileConfiguration == null) {
            this.reload();
        }
        return this.fileConfiguration;
    }
}

