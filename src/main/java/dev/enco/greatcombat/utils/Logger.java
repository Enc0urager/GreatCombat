package dev.enco.greatcombat.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {
    private static final java.util.logging.Logger logger = Bukkit.getLogger();

    public void warn(String message) {
        logger.warning("§7(§eGreatCombat§7) §6WARN §e" + message);
    }

    public void info(String message) {
        logger.info("§7(§aGreatCombat§7) §aINFO §f" + message);
    }

    public void error(String message) {
        logger.warning("§7(§cGreatCombat§7) §4ERROR §c" + message);
    }
}
