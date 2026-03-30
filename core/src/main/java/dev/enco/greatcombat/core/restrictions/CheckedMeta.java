package dev.enco.greatcombat.core.restrictions;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import lombok.Getter;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

@Getter
public enum CheckedMeta implements MetaChecker {

    SIMILAR(false) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemStack().isSimilar(s.itemStack());
        }
    },

    META(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemMeta().equals(s.itemMeta());
        }
    },

    MATERIAL(false) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemStack().getType().equals(s.itemStack().getType());
        }
    },

    ITEM_FLAGS(false) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemStack().getItemFlags().equals(s.itemStack().getItemFlags());
        }
    },

    DISPLAY_NAME(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return Objects.equals(
                    f.itemMeta().getDisplayName(),
                    s.itemMeta().getDisplayName()
            );
        }
    },

    LORE(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            var l1 = f.itemMeta().getLore();
            var l2 = s.itemMeta().getLore();
            return Objects.equals(l1, l2);
        }
    },

    ENCHANTMENTS(false) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemStack().getEnchantments()
                    .equals(s.itemStack().getEnchantments());
        }
    },

    ATTRIBUTES(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return Objects.equals(
                    f.itemMeta().getAttributeModifiers(),
                    s.itemMeta().getAttributeModifiers()
            );
        }
    },

    PDC(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemMeta().getPersistentDataContainer()
                    .equals(s.itemMeta().getPersistentDataContainer());
        }
    },

    UNBREAKABLE(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            return f.itemMeta().isUnbreakable() == s.itemMeta().isUnbreakable();
        }
    },

    POTION_EFFECTS(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            if (!(f.itemMeta() instanceof PotionMeta fm) ||
                    !(s.itemMeta() instanceof PotionMeta sm)) {
                return false;
            }
            return fm.getCustomEffects().equals(sm.getCustomEffects());
        }
    },

    POTION_BASE(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            if (!(f.itemMeta() instanceof PotionMeta fm) ||
                    !(s.itemMeta() instanceof PotionMeta sm)) {
                return false;
            }

            try {
                return Objects.equals(fm.getBasePotionType(), sm.getBasePotionType());
            } catch (NoSuchMethodError e) {
                return Objects.equals(fm.getBasePotionData(), sm.getBasePotionData());
            }
        }
    },

    COLOR(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            if (!(f.itemMeta() instanceof LeatherArmorMeta fm) ||
                    !(s.itemMeta() instanceof LeatherArmorMeta sm)) {
                return false;
            }
            return fm.getColor().equals(sm.getColor());
        }
    },

    CUSTOM_MODEL_DATA(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            var fm = f.itemMeta();
            var sm = s.itemMeta();

            return fm.hasCustomModelData() == sm.hasCustomModelData()
                    && (!fm.hasCustomModelData()
                    || fm.getCustomModelData() == sm.getCustomModelData());
        }
    },

    SKULL(true) {
        @Override
        public boolean hasMeta(IWrappedItem f, IWrappedItem s) {
            if (!(f.itemMeta() instanceof SkullMeta fm) ||
                    !(s.itemMeta() instanceof SkullMeta sm)) {
                return false;
            }
            return Objects.equals(fm.getOwningPlayer(), sm.getOwningPlayer());
        }
    };

    private final boolean requiresMeta;

    CheckedMeta(boolean requiresMeta) {
        this.requiresMeta = requiresMeta;
    }

    @Override
    public boolean requiresMeta() {
        return requiresMeta;
    }
}
