package big2.game.patterns;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * TODO what would be the better name of this class?
 */
public interface CardPatternExhaustedProduct extends Iterable<CardPattern> {

    default Stream<CardPattern> stream() {
        return getCardPatterns().stream();
    }

    Collection<CardPattern> getCardPatterns();

    Collection<CardPattern> getCardPatternsByType(Class<? extends CardPattern> type);

    boolean containCardPatternType(Class type);

    CardPatternOptional lowerCardPattern(CardPattern cardPattern);

    CardPatternOptional higherCardPattern(CardPattern cardPattern);

    CardPatternOptional ceilingCardPattern(CardPattern cardPattern);

    CardPatternOptional floorCardPattern(CardPattern cardPattern);

    CardPattern pollFirstCardPattern();

    CardPattern pollLastCardPattern();
}
