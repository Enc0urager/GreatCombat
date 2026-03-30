package dev.enco.greatcombat.api.models;

import java.util.EnumSet;
import java.util.Set;

public interface ICooldownItem {
    IWrappedItem wrappedItem();
    String translation();
    Set<? extends MetaChecker> checkedMetas();
    EnumSet<InteractionHandler> handlers();
    int time();
    boolean setMaterialCooldown();
    Set<String> linkedItems();
}
