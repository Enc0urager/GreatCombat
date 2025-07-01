package dev.enco.greatcombat.utils.logger;

import dev.enco.greatcombat.utils.logger.impl.ComponentLogger;
import dev.enco.greatcombat.utils.logger.impl.LegacyLogger;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {
    private static final String INFO_PREFIX = "§7(§aGreatCombat§7) §aINFO §f";
    private static final String WARN_PREFIX = "§7(§eGreatCombat§7) §6WARN §e";
    private static final String ERROR_PREFIX = "§7(§cGreatCombat§7) §4ERROR §c";
    private ILogger logger;

    public void setup() {
        logger = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]) >= 19 ? new ComponentLogger() : new LegacyLogger();
    }

    public void warn(String message) {
        logger.warn(WARN_PREFIX + message);
    }

    public void info(String message) {
        logger.info(INFO_PREFIX + message);
    }

    public void error(String message) {
        logger.error(ERROR_PREFIX + message);
    }
}
