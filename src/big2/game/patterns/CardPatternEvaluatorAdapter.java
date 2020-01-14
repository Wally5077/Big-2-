package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

public interface CardPatternEvaluatorAdapter<T extends CardPattern> {

	Class<T> getCardPatternType();

	boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy);

	T create(CardGroup cardGroup, CardPolicy policy);

	Set<T> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy);

}
