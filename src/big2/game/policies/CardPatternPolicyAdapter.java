package big2.game.policies;

import big2.game.patterns.CardPattern;

public interface CardPatternPolicyAdapter<T extends CardPattern> {
    default int getTotalLevel(CardPattern cardPattern) {
        return getCardPatternBaseLevel() + cardPattern.getLevel();
    }

    Class<T> getCardPatternType();

    int getCardPatternBaseLevel();
}
