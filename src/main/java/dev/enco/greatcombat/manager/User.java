package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.api.CombatTickEvent;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Bossbar;
import dev.enco.greatcombat.config.settings.Settings;
import dev.enco.greatcombat.scoreboard.ScoreboardManager;
import dev.enco.greatcombat.utils.Time;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private List<User> opponents = new ArrayList<>();
    private long startPvpTime;
    private final UUID playerUUID;
    private BossBar bossBar;
    private final Bossbar barSettings = ConfigManager.getBossbar();
    private final Settings settings = ConfigManager.getSettings();
    private final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();
    private BukkitRunnable runnable;
    private final PluginManager pm = Bukkit.getPluginManager();

    public Player toPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public void removeFromOpponentsMaps() {
        for (var user : opponents) user.removeOpponent(this);
    }

    public void startTimer() {
        createBossbar();
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                pm.callEvent(new CombatTickEvent(User.this));
            }
        };
        this.runnable.runTaskTimer(GreatCombat.getInstance(),
                0L,
                settings.tickInterval());
    }

    public void refresh(long start) {
        this.startPvpTime = start;
        if (this.runnable != null) runnable.cancel();
        deleteBossbar();
        startTimer();
    }

    public void deleteBossbar() {
        if (bossBar != null) {
            bossBar.removePlayer(Bukkit.getPlayer(getPlayerUUID()));
            this.bossBar = null;
        }
    }

    public void addOpponent(User opponent) {
        this.opponents.add(opponent);
    }

    public void removeOpponent(User opponent) {
        this.opponents.remove(opponent);
    }

    public void createBossbar() {
        if (bossBar == null && barSettings.enable()) {
            bossBar = Bukkit.createBossBar(
                    barSettings.title().replace("{time}", Time.format(settings.combatTime())),
                    barSettings.color(),
                    barSettings.style()
            );
            bossBar.setProgress(1.0);
            bossBar.addPlayer(Bukkit.getPlayer(playerUUID));
            bossBar.setVisible(true);
        }
    }

    public boolean containsOpponent(User user) {
        return this.opponents.contains(user);
    }

    public long getRemaining() {
        long leftTime = System.currentTimeMillis() - startPvpTime;
        long totalTime = settings.combatTime() * 1000;
        return totalTime - leftTime;
    }

    public void updateBoardAndBar(long remainingTime) {
        String time = Time.format((int) remainingTime / 1000);
        ScoreboardManager.setScoreboard(this, time);
        if (this.bossBar != null) {
            bossBar.setTitle(barSettings.title().replace("{time}", time));
            if (barSettings.progress()) {
                double progress = (double) remainingTime / (settings.combatTime() * 1000);
                bossBar.setProgress(progress);
            }
        }
    }
}
