package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.CardPatternExhaustingAdapter;
import big2.game.policies.CardPolicy;

public interface CardPatternEvaluatingAdapter {
	default boolean isMatched(Card[] cards, CardPolicy cardPolicy) {
		return isMatched(new CardGroup(cards), cardPolicy);
	}

	boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy);
	CardPattern create(CardGroup cardGroup, CardPolicy policy);

}
