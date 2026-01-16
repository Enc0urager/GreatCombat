package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.settings.Expansion;
import dev.enco.greatcombat.manager.User;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class PapiExpansion extends PlaceholderExpansion {
    private final Expansion expansion;

    @Override
    public @NotNull String getIdentifier() {
        return "greatcombat";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Encourager";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.9";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        var combatManager = GreatCombat.getInstance().getCombatManager();
        String[] args = params.split("_");
        User user = combatManager.getUser(player.getUniqueId());
        UUID uuid = player.getUniqueId();

        try {
            if (args[0].equals("player")) {
                Player p = Bukkit.getPlayer(args[1]);
                uuid = p.getUniqueId();
                user = combatManager.getUser(p.getUniqueId());
                args = Arrays.copyOfRange(args, 2, args.length);
            }

            switch (args[0]) {
                case "time": {
                    int sec = (int) (user.getRemaining() / 1000L);
                    if (args.length == 1)
                        return String.valueOf(sec);

                    if (args[1].equals("formatted"))
                        return Time.format(sec);
                }
                case "in": {
                    boolean bool = combatManager.isInCombat(uuid);
                    if (args.length == 1) return String.valueOf(bool);
                    if (args[1].equals("formatted")) return bool ? expansion.boolTrue() : expansion.boolFalse();
                }
                case "opponents": {
                    if (args.length == 1) return user.getOpponentsFormatted(expansion.delimiter());
                    if (args[1].equals("contains")) {
                        User opponent = combatManager.getUser(Bukkit.getPlayer(args[2]).getUniqueId());
                        boolean bool = user.containsOpponent(opponent);
                        if (args.length == 3) return String.valueOf(bool);
                        if (args[3].equals("formatted")) return bool ? expansion.boolTrue() : expansion.boolFalse();
                    }
                }
            }

            return expansion.error();

        } catch (Exception e) { return expansion.error(); }
    }
}

