package dev.enco.greatcombat.restrictions.meta;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.MetaChecker;
import dev.enco.greatcombat.restrictions.WrappedItem;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.EnumSet;

@UtilityClass @SuppressWarnings("removal")
public class MetaManager {
    @Getter
    private MetaChecker similarChecker, metaChecker, flagsChecker, materialChecker,
            nameChecker, enchantsChecker, attributesChecker,
            pdcChecker, unbreakableChecker, potionChecker, basePotionChecker,
            colorChecker, modelChecker, loreChecker, skullChecker;

    public void setup() {
        metaChecker = (first, second) ->
                first.itemMeta().equals(second.itemMeta());

        similarChecker = (first, second) ->
            first.itemStack().isSimilar(second.itemStack());

        materialChecker = (first, second) ->
                first.itemStack().getType().equals(second.itemStack().getType());

        flagsChecker = (first, second) ->
                first.itemStack().getItemFlags().equals(second.itemStack().getItemFlags());

        enchantsChecker = (first, second) ->
                first.itemStack().getEnchantments().equals(second.itemStack().getEnchantments());

        nameChecker = (first, second) ->
                first.itemMeta().getDisplayName().equals(second.itemMeta().getDisplayName());

        attributesChecker = (first, second) ->
                first.itemMeta().getAttributeModifiers().equals(second.itemMeta().getAttributeModifiers());

        pdcChecker = (first, second) ->
                first.itemMeta().getPersistentDataContainer().equals(second.itemMeta().getPersistentDataContainer());

        unbreakableChecker = (first, second) ->
                first.itemMeta().isUnbreakable() && second.itemMeta().isUnbreakable();

        potionChecker = (first, second) -> {
            if (!(first.itemMeta() instanceof PotionMeta firstMeta) ||
                    !(second.itemMeta() instanceof PotionMeta secondMeta))
                return false;
            return firstMeta.getCustomEffects().equals(secondMeta.getCustomEffects());
        };

        boolean IS_OLD = false;
        try {
            Class.forName("org.bukkit.potion.PotionData");
            IS_OLD = true;
        } catch (ClassNotFoundException e) {}

        if (IS_OLD) {
            basePotionChecker = (first, second) -> {
                if (!(first.itemMeta() instanceof PotionMeta firstMeta) ||
                        !(second.itemMeta() instanceof PotionMeta secondMeta))
                    return false;
                return firstMeta.getBasePotionData().equals(secondMeta.getBasePotionData());
            };
        } else {
            basePotionChecker = (first, second) -> {
                if (!(first.itemMeta() instanceof PotionMeta firstMeta) ||
                        !(second.itemMeta() instanceof PotionMeta secondMeta))
                    return false;
                return firstMeta.getBasePotionType().equals(secondMeta.getBasePotionType());
            };
        }

        colorChecker = (first, second) -> {
            if (!(first.itemMeta() instanceof LeatherArmorMeta firstMeta) ||
                    !(second.itemMeta() instanceof LeatherArmorMeta secondMeta))
                return false;
            return firstMeta.getColor().equals(secondMeta.getColor());
        };

        modelChecker = (first, second) ->
                first.itemMeta().hasCustomModelData() == second.itemMeta().hasCustomModelData() &&
                first.itemMeta().getCustomModelData() == second.itemMeta().getCustomModelData();

        loreChecker = (first, second) -> {
            var firstLore = first.itemMeta().getLore();
            var secondLore = second.itemMeta().getLore();
            if (firstLore == null && secondLore == null) return true;
            if (firstLore == null && secondLore != null || first != null && secondLore == null) return false;
            return firstLore.equals(secondLore);
        };

        skullChecker = (first, second) -> {
            if (!(first.itemMeta() instanceof SkullMeta firstMeta) ||
                    !(second.itemMeta() instanceof SkullMeta secondMeta))
                return false;
            return firstMeta.getOwningPlayer().equals(secondMeta.getOwningPlayer());
        };
    }

    public boolean isSimilar(ItemStack first, ItemStack second, EnumSet<CheckedMeta> checkedMetas) {
        WrappedItem f = WrappedItem.of(first);
        WrappedItem s = WrappedItem.of(second);
        for (var meta : checkedMetas) {
            if (meta.isCheckMeta()) {
                if (f.hasMeta() && !s.hasMeta() || !f.hasMeta() && s.hasMeta()) return false;
                if (!f.hasMeta() && !s.hasMeta()) continue;
            }
            if (!meta.getChecker().hasMeta(f, s)) return false;
        }
        return true;
    }
}
