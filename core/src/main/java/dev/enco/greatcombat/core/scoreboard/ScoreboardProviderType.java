package dev.enco.greatcombat.core.scoreboard;

import dev.enco.greatcombat.api.models.ScoreboardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.FastBoardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.SternalBoardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.TABProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public enum ScoreboardProviderType {
    TAB(TABProvider.class),
    STERNAL_BOARD(SternalBoardProvider.class),
    FASTBOARD(FastBoardProvider.class);

    public final Class<? extends ScoreboardProvider> providingClass;

    @SneakyThrows
    public ScoreboardProvider getProvider() {
        return providingClass.newInstance();
    }
}
