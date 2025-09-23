package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.scheduler.TaskManager;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

@UtilityClass
public class UpdateUtils {
     public void check(Consumer<String> consumer) {
         TaskManager.getGlobalScheduler().runLaterAsync(() -> {
             try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                     new URL("https://raw.githubusercontent.com/Enc0urager/GreatCombat/master/VERSION")
                             .openStream()))) {
                 consumer.accept(reader.readLine().trim());
             } catch (IOException e) {
                 Logger.warn(ConfigManager.getLocale().errorUpdates() + e);
             }
         }, 30L);
     }

     // Нагло спизжено отсюда -
     // https://github.com/Overwrite987/UltimateServerProtector/blob/main/src/main/java/ru/overwrite/protect/bukkit/commands/subcommands/UpdateSubcommand.java
     public void update(String ver, CommandSender sender) {
         String currentJarName = new File(GreatCombat.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
         String downloadUrl = "https://github.com/Enc0urager/GreatCombat/releases/download/" + ver + "/" + "GreatCombat-" + ver + ".jar";
         File updateFolder = new File("plugins");
         File targetFile = new File(updateFolder, currentJarName);
         downloadFile(downloadUrl, targetFile, sender);
     }

    @SneakyThrows
    private void downloadFile(String fileURL, File targetFile, CommandSender sender) {
        URL url = new URL(fileURL);
        URLConnection connection = url.openConnection();
        int fileSize = connection.getContentLength();

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(targetFile)) {

            byte[] data = new byte[1024];
            int bytesRead;
            int totalBytesRead = 0;
            int lastPercentage = 0;

            while ((bytesRead = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, bytesRead);
                totalBytesRead += bytesRead;
                int progressPercentage = (int) ((double) totalBytesRead / fileSize * 100);

                if (progressPercentage >= lastPercentage + 10) {
                    lastPercentage = progressPercentage;
                    int downloadedKB = totalBytesRead / 1024;
                    int fullSizeKB = fileSize / 1024;
                    sender.sendMessage(downloadedKB + "/" + fullSizeKB + "KB (" + progressPercentage + "%)");
                }
            }
        }
    }
}
