package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.policies.Big2Policy;

import java.util.Collection;
import java.util.stream.Collectors;

public class CardPatternEvaluator {
	private Big2Policy big2Policy;
	private Collection<CardPatternEvaluatorAdapter<? extends CardPattern>> cardPatternEvaluatorAdapters;

	public CardPatternEvaluator(Big2Policy big2Policy,
								Collection<CardPatternEvaluatorAdapter<? extends CardPattern>>
										cardPatternEvaluatorAdapters) {
		this.big2Policy = big2Policy;
		this.cardPatternEvaluatorAdapters = cardPatternEvaluatorAdapters;
	}

	public CardPattern evaluate(CardGroup cardGroup) {
		return cardPatternEvaluatorAdapters.stream()
				.filter(adapter -> adapter.isMatched(cardGroup, big2Policy))
				.findFirst()
				.map(adapter -> adapter.create(cardGroup, big2Policy))
				.orElseThrow(CardPatternInvalidException::new);
	}

	public CardPatternEnumeration enumerateCardPatterns(CardGroup cardGroup) {
		return new EagerCardPatternEnumeration(
				cardPatternEvaluatorAdapters.stream()
				.map(adapter -> adapter.enumerateCardPatterns(cardGroup, big2Policy))
				.flatMap(Collection::stream)
				.collect(Collectors.toList()),
				big2Policy);
	}

	public Big2Policy getBig2Policy() {
		return big2Policy;
	}
}
