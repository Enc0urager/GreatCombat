package dev.enco.greatcombat.powerups;

import lombok.Getter;

@Getter
public enum PowerupType {
    FLY,
    GOD,
    VANISH,
    GAMEMODE,
    WALKSPEED;

    private PowerupChecker powerupChecker;
    private PowerupDisabler powerupDisabler;

    public void initialize(ServerManager serverManager) {
        switch (this) {
            case FLY:
                this.powerupDisabler = serverManager.flyDisabler();
                this.powerupChecker = serverManager.flyChecker();
                break;
            case GOD:
                this.powerupDisabler = serverManager.godDisabler();
                this.powerupChecker = serverManager.godChecker();
                break;
            case VANISH:
                this.powerupDisabler = serverManager.vanishDisabler();
                this.powerupChecker = serverManager.vanishChecker();
                break;
            case GAMEMODE:
                this.powerupDisabler = serverManager.gamemodeDisabler();
                this.powerupChecker = serverManager.gamemodeChecker();
                break;
            case WALKSPEED:
                this.powerupDisabler = serverManager.walkspeedDisabler();
                this.powerupChecker = serverManager.walkspeedChecker();
                break;
        }
    }
}
