package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.logger.Logger;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

public class UpdateChecker {
     public UpdateChecker(GreatCombat plugin, Consumer<String> consumer) {
         Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
             try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                     new URL("https://raw.githubusercontent.com/Enc0urager/GreatCombat/master/VERSION")
                             .openStream()))) {
                 consumer.accept(reader.readLine().trim());
             } catch (IOException e) {
                 Logger.warn(ConfigManager.getLocale().errorUpdates() + e);
             }
         }, 30L);
     }
}
