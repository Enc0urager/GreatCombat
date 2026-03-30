package dev.enco.greatcombat.api.models;

import java.util.EnumSet;
import java.util.Set;

public interface IPreventableItem {
    IWrappedItem wrappedItem();
    String translation();
    EnumSet<PreventionType> types();
    EnumSet<InteractionHandler> handlers();
    Set<? extends MetaChecker> checkedMetas();
    boolean setMaterialCooldown();
}
