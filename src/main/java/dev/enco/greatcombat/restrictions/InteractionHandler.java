package dev.enco.greatcombat.restrictions;

/**
 * Enum representing different types of item interactions that can be handled.
 */
public enum InteractionHandler {
    /**
     * Item consumption (eating, drinking)
     */
    CONSUME,

    /**
     * Right click in air
     */
    RIGHT_CLICK_AIR,

    /**
     * Right click on block
     */
    RIGHT_CLICK_BLOCK,

    /**
     * Left click in air
     */
    LEFT_CLICK_AIR,

    /**
     * Left click on block
     */
    LEFT_CLICK_BLOCK,

    /**
     * Block breaking
     */
    BLOCK_BREAK,

    /**
     * Mainhand item resurrection (totem)
     */
    RESURRECT_MAINHAND,

    /**
     * Offhand item resurrection (totem)
     */
    RESURRECT_OFFHAND,

    /**
     * Bow shooting
     */
    BOW_SHOOT,

    /**
     * Projectile lauch
     */
    PROJECTILE_LAUNCH
}
