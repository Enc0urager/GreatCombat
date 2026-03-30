package dev.enco.greatcombat.core.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.models.PowerupType;
import dev.enco.greatcombat.core.actions.ActionFactory;
import dev.enco.greatcombat.core.config.settings.*;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.listeners.CommandsType;
import dev.enco.greatcombat.core.utils.EnumUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.Placeholders;
import dev.enco.greatcombat.core.utils.Time;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import dev.enco.greatcombat.core.utils.logger.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Getter
public class ConfigManager {
    private final IRegionManager regionManager;
    private final JavaPlugin plugin;
    private boolean checkUpdates, metricsEnable, elytraGlideAllowed, usingPapi;
    private Commands commands;
    private Scoreboard scoreboard;
    private Powerups powerups;
    private Messages messages;
    private TimeFormats secondsFormats, minutesFormats, hoursFormats;
    private Bossbar bossbar;
    private Settings settings;
    private String serverManager;
    private Locale locale;
    private FileConfiguration mainConfig;
    private Expansion expansion;
    private List<String> regionWorlds;

    public void reload() {
        FilesHandler.reloadAll();
        load();
    }

    public void load() {
        long start = System.currentTimeMillis();
        mainConfig = FilesHandler.getConfigFile("config").get();
        metricsEnable = mainConfig.getBoolean("metrics");
        setupLogger();
        LangUtils.setup(this);
        Time.setConfigManager(this);
        var colorizerSection = mainConfig.getConfigurationSection("colorizer");
        if (colorizerSection == null) mainConfig.createSection("colorizer");
        Colorizer.setColorizer(mainConfig.getString("colorizer", "LEGACY"));
        setupScoreboard();
        var messagesConfig = FilesHandler.getConfigFile("messages").get();
        setupActions(messagesConfig);
        setupBossbar(messagesConfig);
        setupTimeFormats(mainConfig);
        setupPowerups(mainConfig);
        setupSettings(mainConfig);
        setupCommands(mainConfig);
        checkUpdates = mainConfig.getBoolean("update-checker");
        usingPapi = mainConfig.getBoolean("use-papi");
        Placeholders.setUsingPapi(usingPapi);
        var expansionSec = mainConfig.getConfigurationSection("expansion");
        expansion = new Expansion(
                expansionSec.getString("error"),
                expansionSec.getString("true"),
                expansionSec.getString("false"),
                expansionSec.getString("delimiter")
        );
        regionWorlds = mainConfig.getStringList("region-worlds");
        LangUtils.shutdown(mainConfig.getBoolean("disable-lang"));
        elytraGlideAllowed = mainConfig.getBoolean("allow-elytra-gliding");
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
                EnumUtils.toEnumSet(config.getStringList("allowed-teleportations-cause"), PlayerTeleportEvent.TeleportCause.class, c -> Logger.warn("Unknown teleport cause")),
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

        ImmutableMap.Builder<String, Set<String>> mapBuilder = ImmutableMap.builder();
        List<Map<?, ?>> commandMaps = section.getMapList("commands");

        for (var map : commandMaps) {
            for (var entry : map.entrySet()) {
                var cmd = entry.getKey().toString();
                var subCmds = (List<String>) entry.getValue();
                mapBuilder.put(cmd, new HashSet<>(subCmds));
            }
        }
        commands = new Commands(
                CommandsType.valueOf(section.getString("type")),
                section.getBoolean("change-tabcomplete"),
                mapBuilder.build(),
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
                ActionFactory.from(locale, section.getStringList("on-start-damager")),
                ActionFactory.from(locale, section.getStringList("on-start-target")),
                ActionFactory.from(locale, section.getStringList("on-stop")),
                ActionFactory.from(locale, section.getStringList("on-item-cooldown")),
                ActionFactory.from(locale, section.getStringList("on-pvp-leave")),
                ActionFactory.from(locale, section.getStringList("on-pvp-command")),
                ActionFactory.from(locale, section.getStringList("on-interact-prevention")),
                ActionFactory.from(locale, section.getStringList("on-tick")),
                ActionFactory.from(locale, section.getStringList("on-player-command")),
                ActionFactory.from(locale, section.getStringList("on-join")),
                ActionFactory.from(locale, section.getStringList("on-merge")),
                ActionFactory.from(locale, section.getStringList("on-cooldown-expired")),
                ActionFactory.from(locale, section.getStringList("on-held"))
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
