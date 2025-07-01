package dev.enco.greatcombat.utils.logger.impl;

import dev.enco.greatcombat.utils.logger.ILogger;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class LegacyLogger implements ILogger {
    private final Logger logger;

    public LegacyLogger() {
        this.logger = Bukkit.getLogger();
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void warn(String s) {
        logger.warning(s);
    }

    @Override
    public void error(String s) {
        logger.severe(s);
    }
}
