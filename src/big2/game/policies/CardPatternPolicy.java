package big2.game.policies;

import big2.game.patterns.CardPattern;

import java.util.NavigableSet;

public interface CardPatternPolicy {
    int compare(CardPattern cardPattern1, CardPattern cardPattern2);
    int compare(Class<? extends CardPattern> type1, Class<? extends CardPattern> type2);
    NavigableSet<Class<? extends CardPattern>> getOrderedCardPatternTypes();
}
