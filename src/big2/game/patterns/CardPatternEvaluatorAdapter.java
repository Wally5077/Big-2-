package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

public interface CardPatternEvaluatorAdapter {
	default boolean isMatched(Card[] cards, CardPolicy cardPolicy) {
		return isMatched(new CardGroup(cards), cardPolicy);
	}

	boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy);
	CardPattern create(CardGroup cardGroup, CardPolicy policy);
	Set<? extends CardPattern> exhaustCardPatterns(CardGroup cards, CardPolicy cardPolicy);
}
