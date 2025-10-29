package dev.enco.greatcombat.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.actions.ActionFactory;
import dev.enco.greatcombat.config.settings.*;
import dev.enco.greatcombat.listeners.CommandsType;
import dev.enco.greatcombat.powerups.PowerupType;
import dev.enco.greatcombat.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.restrictions.prevention.PreventionManager;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.utils.EnumUtils;
import dev.enco.greatcombat.utils.LangUtils;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor
public class ConfigManager {
    private final GreatCombat plugin;
    @Getter
    private static boolean checkUpdates;
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
    @Getter
    private static Locale locale;
    @Getter
    private static FileConfiguration mainConfig;

    public void reload() {
        FilesHandler.reloadAll();
        load();
    }

    public void load() {
        long start = System.currentTimeMillis();
        mainConfig = FilesHandler.getConfigFile("config").get();
        metricsEnable = mainConfig.getBoolean("metrics");
        setupLogger();
        LangUtils.setup();
        var colorizerSection = mainConfig.getConfigurationSection("colorizer");
        if (colorizerSection == null) mainConfig.createSection("colorizer");
        Colorizer.setColorizer(mainConfig.getString("colorizer", "LEGACY"));
        setupScoreboard();
        var messagesConfig = FilesHandler.getConfigFile("messages").get();
        setupActions(messagesConfig);
        setupBossbar(messagesConfig);
        CooldownManager.setupCooldownItems(mainConfig);
        setupTimeFormats(mainConfig);
        setupPowerups(mainConfig);
        setupSettings(mainConfig);
        setupCommands(mainConfig);
        ScoreboardManager.setProvider(mainConfig.getString("scoreboard-manager"));
        checkUpdates = mainConfig.getBoolean("update-checker");
        PreventionManager.load(mainConfig.getConfigurationSection("preventable-items"));
        usingPapi = mainConfig.getBoolean("use-papi");
        teleportEnable = mainConfig.getBoolean("allow-teleport");
        LangUtils.shutdown(mainConfig.getBoolean("disable-lang"));
        Logger.info(locale.configLoaded() + (System.currentTimeMillis() - start) + " ms.");
    }

    public boolean checkAndCreateLangFiles() {
        File languageFile = new File(plugin.getDataFolder(), "locale.yml");
        if (!languageFile.exists()) {
            plugin.saveResource("locale.yml", false);
            Logger.error("Выберите язык в файле locale.yml и перезапустите сервер");
            Logger.error("Change language in file locale.yml and reboot server");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        FileConfiguration landConfig = YamlConfiguration.loadConfiguration(languageFile);
        String lang = landConfig.getString("locale");
        boolean safe = landConfig.getBoolean("replace");
        final var folder = plugin.getDataFolder();
        FilesHandler.addConfigFile2List(new ConfigFile("messages", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("config", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("scoreboard", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("logger", folder, lang, safe));
        return true;
    }

    private void setupLogger() {
        var lang = FilesHandler.getConfigFile("logger").get();
        locale = new Locale(
                lang.getString("on-enable"),
                lang.getString("on-disable"),
                lang.getString("author-ver"),
                lang.getString("config-loaded"),
                lang.getStringList("updates-found"),
                lang.getString("updates-not-found"),
                lang.getString("updates-error"),
                lang.getStringList("outdated-core"),
                lang.getString("tab-discarded-instance"),
                lang.getString("scoreboard-provider"),
                lang.getString("scoreboard-error"),
                lang.getString("handler-does-not-exist"),
                lang.getString("meta-does-not-exist"),
                lang.getString("blocker-does-not-exist"),
                lang.getString("powerup-does-not-exist"),
                lang.getString("server-manager-loading"),
                lang.getString("server-manager-loaded"),
                lang.getString("server-manager-error"),
                lang.getString("bar-color-error"),
                lang.getString("bar-style-error"),
                lang.getString("projectile-error"),
                lang.getStringList("command-help"),
                lang.getString("player-not-found"),
                lang.getString("specify-player"),
                lang.getString("not-in-combat"),
                lang.getString("stop-success"),
                lang.getString("stopall-success"),
                lang.getString("empty-item"),
                lang.getString("click-to-copy"),
                lang.getString("specify-2-players"),
                lang.getString("combat-started"),
                lang.getString("illegal-action-pattern"),
                lang.getString("action-does-not-exist"),
                lang.getString("sound-does-not-exist"),
                lang.getString("volume-and-pitch-error"),
                lang.getString("null-sound"),
                lang.getString("reload"),
                lang.getString("updated"),
                lang.getString("material-null"),
                lang.getString("material-error"),
                lang.getString("lang-error"),
                lang.getString("lang-success")
        );
    }

    private void setupScoreboard() {
        var scoreboardConfig = FilesHandler.getConfigFile("scoreboard").get();
        scoreboard = new Scoreboard(
                Colorizer.colorize(scoreboardConfig.getString("title")),
                ImmutableList.copyOf(Colorizer.colorizeAll(scoreboardConfig.getStringList("lines"))),
                Colorizer.colorize(scoreboardConfig.getString("opponent")),
                Colorizer.colorize(scoreboardConfig.getString("empty")),
                scoreboardConfig.getBoolean("enable")
        );
    }

    private void setupSettings(FileConfiguration config) {
        List<EntityType> ignored = new ArrayList<>();
        for (var type : config.getStringList("ignored-projectile")) {
            try {
                ignored.add(EntityType.valueOf(type));
            } catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(locale.projectileError(), type));
            }
        }
        settings = new Settings(
                config.getInt("pvp-time"),
                config.getBoolean("allow-teleport"),
                ImmutableSet.copyOf(config.getStringList("ignored-worlds")),
                config.getBoolean("kill-on-leave"),
                config.getBoolean("kill-on-kick"),
                ImmutableSet.copyOf(config.getStringList("kick-messages")),
                config.getLong("tick-interval"),
                config.getLong("time-to-stop"),
                ImmutableSet.copyOf(ignored)
        );
    }

    private void setupPowerups(FileConfiguration config) {
        serverManager = config.getString("server-manager");
        powerups = new Powerups(
                EnumUtils.toEnumSet(config.getStringList("prevent-start-if-damager"), PowerupType.class, s ->
                        Logger.warn(locale.powerupTypeDoesNotExist())),
                EnumUtils.toEnumSet(config.getStringList("prevent-start-if-target"), PowerupType.class, s ->
                        Logger.warn(locale.powerupTypeDoesNotExist())),
                EnumUtils.toEnumSet(config.getStringList("disable-for-damager"), PowerupType.class, s ->
                        Logger.warn(locale.powerupTypeDoesNotExist())),
                EnumUtils.toEnumSet(config.getStringList("disable-for-target"), PowerupType.class, s ->
                        Logger.warn(locale.powerupTypeDoesNotExist()))
        );
    }

    private void setupCommands(FileConfiguration config) {
        var section = config.getConfigurationSection("commands");
        commands = new Commands(
                CommandsType.valueOf(section.getString("type")),
                section.getBoolean("change-tabcomplete"),
                ImmutableSet.copyOf(section.getStringList("commands")),
                ImmutableSet.copyOf(section.getStringList("player-commands"))
        );
    }

    private void setupBossbar(FileConfiguration config) {
        var section = config.getConfigurationSection("bossbar");
        var style = BarStyle.SOLID;
        String st = section.getString("style");
        try {
            style = BarStyle.valueOf(st);
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.barStyleError(), st));
        }
        var color = BarColor.RED;
        String c = section.getString("color");
        try {
            color = BarColor.valueOf(c);
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.barColorError(), c));
        }
        this.bossbar = new Bossbar(
                style,
                color,
                Colorizer.colorize(section.getString("title")),
                section.getBoolean("progress"),
                section.getBoolean("enable")
        );
    }

    private void setupActions(FileConfiguration config) {
        var section = config.getConfigurationSection("actions");
        this.messages = new Messages(
                ActionFactory.from(section.getStringList("on-start-damager")),
                ActionFactory.from(section.getStringList("on-start-target")),
                ActionFactory.from(section.getStringList("on-stop")),
                ActionFactory.from(section.getStringList("on-item-cooldown")),
                ActionFactory.from(section.getStringList("on-pvp-leave")),
                ActionFactory.from(section.getStringList("on-pvp-command")),
                ActionFactory.from(section.getStringList("on-interact-prevention")),
                ActionFactory.from(section.getStringList("on-tick")),
                ActionFactory.from(section.getStringList("on-player-command")),
                ActionFactory.from(section.getStringList("on-join")),
                ActionFactory.from(section.getStringList("on-merge"))
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
