package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

public interface CardPatternEvaluatorAdapter {
	boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy);
	CardPattern create(CardGroup cardGroup, CardPolicy policy);
	Set<? extends CardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy);
}
