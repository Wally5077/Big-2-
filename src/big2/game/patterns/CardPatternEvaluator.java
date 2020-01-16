package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.policies.Big2Policy;

import java.util.Collection;

public class CardPatternEvaluator {
	private Big2Policy big2Policy;
	private Collection<CardPatternEvaluatorAdapter> evaluatorAdapters;

	public CardPatternEvaluator(Big2Policy big2Policy,
								Collection<CardPatternEvaluatorAdapter>
										evaluatorAdapters) {
		this.big2Policy = big2Policy;
		this.evaluatorAdapters = evaluatorAdapters;
	}

	public CardPattern evaluate(CardGroup cardGroup) {
		return evaluatorAdapters.stream()
				.filter(adapter -> adapter.isMatched(cardGroup, big2Policy))
				.findFirst()
				.map(adapter -> adapter.create(cardGroup, big2Policy))
				.orElseThrow(CardPatternInvalidException::new);
	}

	public CardPatternExhaustion enumerateCardPatterns(CardGroup cardGroup) {
		return new EagerCardPatternExhaustion(evaluatorAdapters, cardGroup, big2Policy);
	}

	public Big2Policy getBig2Policy() {
		return big2Policy;
	}
}
