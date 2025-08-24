package dev.enco.greatcombat.restrictions;

import dev.enco.greatcombat.restrictions.meta.MetaManager;
import lombok.Getter;

@Getter
public enum CheckedMeta {
    SIMILAR(MetaManager.getSimilarChecker(), false),
    META(MetaManager.getMetaChecker(), true),
    MATERIAL(MetaManager.getMaterialChecker(), false),
    ITEM_FLAGS(MetaManager.getFlagsChecker(), false),
    DISPLAY_NAME(MetaManager.getNameChecker(), true),
    LORE(MetaManager.getLoreChecker(), true),
    ENCHANTMENTS(MetaManager.getEnchantsChecker(), false),
    ATTRIBUTES(MetaManager.getAttributesChecker(), true),
    PDC(MetaManager.getPdcChecker(), true),
    UNBREAKABLE(MetaManager.getUnbreakableChecker(), true),
    POTION_EFFECTS(MetaManager.getPotionChecker(), true),
    POTION_BASE(MetaManager.getBasePotionChecker(), false),
    COLOR(MetaManager.getColorChecker(), true),
    CUSTOM_MODEL_DATA(MetaManager.getModelChecker(), true),
    SKULL(MetaManager.getSkullChecker(), true);

    final MetaChecker checker;
    final boolean checkMeta;

    CheckedMeta(MetaChecker checker, boolean checkMeta) {
        this.checker = checker;
        this.checkMeta = checkMeta;
    }
}
