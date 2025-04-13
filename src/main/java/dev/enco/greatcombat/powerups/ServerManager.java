package dev.enco.greatcombat.powerups;

public interface ServerManager {
    void setup();
    PowerupDisabler flyDisabler();
    PowerupDisabler godDisabler();
    PowerupDisabler vanishDisabler();
    PowerupDisabler gamemodeDisabler();
    PowerupDisabler walkspeedDisabler();
    PowerupChecker flyChecker();
    PowerupChecker godChecker();
    PowerupChecker vanishChecker();
    PowerupChecker gamemodeChecker();
    PowerupChecker walkspeedChecker();
}
