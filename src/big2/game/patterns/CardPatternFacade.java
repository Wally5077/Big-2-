package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.policies.Big2Policy;

import java.util.Collection;
import java.util.stream.Collectors;

public class CardPatternFacade {
	private Big2Policy big2Policy;
	private Collection<CardPatternAdapter> evaluatorAdapters;

	public CardPatternFacade(Big2Policy big2Policy,
							 Collection<CardPatternAdapter>
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

	public CardPatternExhaustedProduct exhaustCardPatterns(CardGroup cardGroup) {
		return new WrappingCardPatternExhaustedProduct(
				evaluatorAdapters.stream()
				.flatMap(adapter -> adapter.exhaustCardPatterns(cardGroup, big2Policy).stream())
				.collect(Collectors.toList()), big2Policy);
	}

	public Big2Policy getBig2Policy() {
		return big2Policy;
	}
}
