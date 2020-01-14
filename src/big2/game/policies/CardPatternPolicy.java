package big2.game.policies;

import big2.game.patterns.CardPattern;

import java.util.Collection;

public interface CardPatternPolicy {
    int getTotalLevel(CardPattern cardPattern);

    default int getCardPatternBaseLevel(CardPattern cardPattern) {
        return getCardPatternBaseLevel(cardPattern.getClass());
    }

    int getCardPatternBaseLevel(Class<? extends CardPattern> type);

    /**
     * @param type the base type
     * @return all higher level card pattern types compared to the base type
     */
    Collection<Class<? extends CardPattern>> getCeilingLevelCardPatternTypes(Class<? extends CardPattern> type);


    /**
     * @param type the base type
     * @return all lower level card pattern types compared to the base type
     */
    Collection<Class<? extends CardPattern>> getFloorLevelCardPatternTypes(Class<? extends CardPattern> type);

}
