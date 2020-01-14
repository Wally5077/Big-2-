package big2.game.patterns;

import big2.game.patterns.CardPattern;

import java.util.List;

public interface CardPatternEnumeration {
    List<CardPattern> getCardPatternsByType(Class<? extends CardPattern> type);

    boolean containCardPatternType(Class type);

    CardPattern lowerCardPattern(CardPattern cardPattern);

    CardPattern higherCardPattern(CardPattern cardPattern);

    CardPattern ceilingCardPattern(CardPattern cardPattern);

    CardPattern floorCardPattern(CardPattern cardPattern);

    CardPattern pollFirstCardPattern();

    CardPattern pollLastCardPattern();

}
