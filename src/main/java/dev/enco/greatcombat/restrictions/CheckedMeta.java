package dev.enco.greatcombat.restrictions;

import dev.enco.greatcombat.restrictions.meta.MetaManager;
import lombok.Getter;

/**
 * Enum representing different types of item metadata that can be checked for matching.
 * Each enum value has an associated MetaChecker and flag indicating if it requires item meta.
 */
@Getter
public enum CheckedMeta {
    /**
     * Checks if items are similar (similar to isSimilar() method)
     */
    SIMILAR(MetaManager.getSimilarChecker(), false),

    /**
     * Checks if item meta is exactly equal
     */
    META(MetaManager.getMetaChecker(), true),

    /**
     * Checks if materials are the same
     */
    MATERIAL(MetaManager.getMaterialChecker(), false),

    /**
     * Checks if item flags are the same
     */
    ITEM_FLAGS(MetaManager.getFlagsChecker(), false),

    /**
     * Checks if display names are the same
     */
    DISPLAY_NAME(MetaManager.getNameChecker(), true),

    /**
     * Checks if lore is the same
     */
    LORE(MetaManager.getLoreChecker(), true),

    /**
     * Checks if enchantments are the same
     */
    ENCHANTMENTS(MetaManager.getEnchantsChecker(), false),

    /**
     * Checks if attributes are the same
     */
    ATTRIBUTES(MetaManager.getAttributesChecker(), true),

    /**
     * Checks if PersistentDataContainer is the same
     */
    PDC(MetaManager.getPdcChecker(), true),

    /**
     * Checks if unbreakable status is the same
     */
    UNBREAKABLE(MetaManager.getUnbreakableChecker(), true),

    /**
     * Checks if potion effects are the same
     */
    POTION_EFFECTS(MetaManager.getPotionChecker(), true),

    /**
     * Checks if base potion type is the same
     */
    POTION_BASE(MetaManager.getBasePotionChecker(), false),

    /**
     * Checks if leather armor color is the same
     */
    COLOR(MetaManager.getColorChecker(), true),

    /**
     * Checks if custom model data is the same
     */
    CUSTOM_MODEL_DATA(MetaManager.getModelChecker(), true),

    /**
     * Checks if skull owner is the same
     */
    SKULL(MetaManager.getSkullChecker(), true);

    final MetaChecker checker;
    final boolean checkMeta;

    CheckedMeta(MetaChecker checker, boolean checkMeta) {
        this.checker = checker;
        this.checkMeta = checkMeta;
    }
}
