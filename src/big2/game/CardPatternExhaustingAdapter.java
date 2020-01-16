package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.policies.CardPolicy;

import java.util.Set;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface CardPatternExhaustingAdapter {
    Set<? extends CardPattern> exhaustCardPatterns(CardGroup cards, CardPolicy cardPolicy);
}
