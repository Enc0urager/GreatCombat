package dev.enco.greatcombat.config;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.actions.ActionRegistry;
import dev.enco.greatcombat.config.settings.*;
import dev.enco.greatcombat.cooldowns.CooldownManager;
import dev.enco.greatcombat.listeners.CommandsType;
import dev.enco.greatcombat.powerups.PowerupsManager;
import dev.enco.greatcombat.utils.Colorizer;
import dev.enco.greatcombat.utils.Logger;
import lombok.Getter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigManager {
    private final GreatCombat plugin;
    @Getter
    private static boolean teleportEnable;
    @Getter
    private static boolean metricsEnable;
    @Getter
    private static Commands commands;
    @Getter
    private static Scoreboard scoreboard;
    @Getter
    private static Powerups powerups;
    @Getter
    private static Messages messages;
    @Getter
    private static TimeFormats secondsFormats;
    @Getter
    private static TimeFormats minutesFormats;
    @Getter
    private static TimeFormats hoursFormats;
    @Getter
    private static Bossbar bossbar;
    @Getter
    private static Settings settings;
    @Getter
    private static String serverManager;
    @Getter
    private static boolean usingPapi;

    public ConfigManager(GreatCombat plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        long start = System.currentTimeMillis();
        setupConfigFiles();
        setupScoreboard();
        var messagesConfig = FilesHandler.getConfigFile("messages").get();
        setupBossbar(messagesConfig);
        setupActions(messagesConfig);
        var mainConfig = FilesHandler.getConfigFile("config").get();
        metricsEnable = mainConfig.getBoolean("metrics");
        CooldownManager.setupCooldownItems(mainConfig);
        setupTimeFormats(mainConfig);
        setupPowerups(mainConfig);
        setupSettings(mainConfig);
        setupCommands(mainConfig);
        usingPapi = mainConfig.getBoolean("use-papi");
        teleportEnable = mainConfig.getBoolean("allow-teleport");
        Logger.info("Конфиг загружен за " + (System.currentTimeMillis() - start) + " ms.");
    }

    private void setupConfigFiles() {
        FilesHandler.addConfigFile2List(new ConfigFile("messages", plugin.getDataFolder()));
        FilesHandler.addConfigFile2List(new ConfigFile("config", plugin.getDataFolder()));
        FilesHandler.addConfigFile2List(new ConfigFile("scoreboard", plugin.getDataFolder()));
    }

    private void setupScoreboard() {
        var scoreboardConfig = FilesHandler.getConfigFile("scoreboard").get();
        this.scoreboard = new Scoreboard(
                scoreboardConfig.getString("title"),
                scoreboardConfig.getStringList("lines"),
                scoreboardConfig.getString("opponent"),
                scoreboardConfig.getString("empty"),
                scoreboardConfig.getBoolean("enable")
        );
    }

    private void setupSettings(FileConfiguration config) {
        settings = new Settings(
                config.getInt("pvp-time"),
                config.getBoolean("allow-teleport"),
                config.getStringList("ignored-worlds"),
                config.getBoolean("kill-on-leave"),
                config.getBoolean("kill-on-kick"),
                config.getStringList("kick-messages")
        );
    }

    private void setupPowerups(FileConfiguration config) {
        serverManager = config.getString("server-manager");
        powerups = new Powerups(
                PowerupsManager.transform(Colorizer.colorizeAll(config.getStringList("prevent-start-if-damager"))),
                PowerupsManager.transform(Colorizer.colorizeAll(config.getStringList("prevent-start-if-target"))),
                PowerupsManager.transform(Colorizer.colorizeAll(config.getStringList("disable-for-damager"))),
                PowerupsManager.transform(Colorizer.colorizeAll(config.getStringList("disable-for-target")))
        );
    }

    private void setupCommands(FileConfiguration config) {
        var section = config.getConfigurationSection("commands");
        commands = new Commands(
                CommandsType.valueOf(section.getString("type")),
                section.getBoolean("change-tabcomplete"),
                section.getStringList("commands")
        );
    }

    private void setupBossbar(FileConfiguration config) {
        var section = config.getConfigurationSection("bossbar");
        var style = BarStyle.SOLID;
        String st = section.getString("style");
        try {
            style = BarStyle.valueOf(st);
        } catch (IllegalArgumentException e) {
            Logger.warn("Bar style " + st + " is not available, using SOLID");
        }
        var color = BarColor.RED;
        String c = section.getString("color");
        try {
            color = BarColor.valueOf(c);
        } catch (IllegalArgumentException e) {
            Logger.warn("Bar color " + c + " is not available, using RED");
        }
        this.bossbar = new Bossbar(
                style,
                color,
                Colorizer.colorize(section.getString("title")),
                section.getBoolean("progress"),
                section.getLong("progress-update-interval"),
                section.getBoolean("enable")
        );
    }

    private void setupActions(FileConfiguration config) {
        var section = config.getConfigurationSection("actions");
        this.messages = new Messages(
                ActionRegistry.transform(section.getStringList("on-start-damager")),
                ActionRegistry.transform(section.getStringList("on-start-target")),
                ActionRegistry.transform(section.getStringList("on-stop")),
                ActionRegistry.transform(section.getStringList("on-item-cooldown")),
                ActionRegistry.transform(section.getStringList("on-pvp-leave")),
                ActionRegistry.transform(section.getStringList("on-pvp-command"))
        );
    }

    private void setupTimeFormats(FileConfiguration config) {
        var section = config.getConfigurationSection("time");
        var seconds = section.getConfigurationSection("seconds");
        var minutes = section.getConfigurationSection("minutes");
        var hours = section.getConfigurationSection("hours");
        secondsFormats = new TimeFormats(
                seconds.getString("form1"),
                seconds.getString("form3"),
                seconds.getString("form5")
        );
        minutesFormats = new TimeFormats(
                minutes.getString("form1"),
                minutes.getString("form3"),
                minutes.getString("form5")
        );
        hoursFormats = new TimeFormats(
                hours.getString("form1"),
                hours.getString("form3"),
                hours.getString("form5")
        );
    }
}
