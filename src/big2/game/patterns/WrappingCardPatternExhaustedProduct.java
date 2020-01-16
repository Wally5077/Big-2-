package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.CardPatternExhaustingAdapter;
import big2.game.policies.Big2Policy;

import java.util.*;
import java.util.stream.Collectors;

public class WrappingCardPatternExhaustedProduct implements CardPatternExhaustedProduct {
	private Map<Class<? extends CardPattern>, List<CardPattern>> cardPatternMap;
	private TreeSet<CardPattern> cardPatternTreeSet;

	public WrappingCardPatternExhaustedProduct(Collection<CardPattern> cardPatterns,
											   Big2Policy big2Policy) {
		cardPatternTreeSet = new TreeSet<>(big2Policy::compare);
		cardPatternTreeSet.addAll(cardPatterns);
		cardPatternMap = cardPatternTreeSet.stream().collect(Collectors.groupingBy(CardPattern::getClass));
	}

	@Override
	public Collection<CardPattern> getCardPatterns() {
		return cardPatternTreeSet;
	}

	@Override
	public List<CardPattern> getCardPatternsByType(Class<? extends CardPattern> type) {
		return cardPatternMap.get(type);
	}

	@Override
	public boolean containCardPatternType(Class type) {
		return cardPatternMap.containsKey(type);
	}

	@Override
	public CardPatternOptional lowerCardPattern(CardPattern cardPattern) {
		return CardPatternOptional.ofNullable(cardPatternTreeSet.lower(cardPattern));
	}

	@Override
	public CardPatternOptional higherCardPattern(CardPattern cardPattern) {
		return CardPatternOptional.ofNullable(cardPatternTreeSet.higher(cardPattern));
	}

	@Override
	public CardPatternOptional ceilingCardPattern(CardPattern cardPattern) {
		return CardPatternOptional.ofNullable(cardPatternTreeSet.ceiling(cardPattern));
	}

	@Override
	public CardPatternOptional floorCardPattern(CardPattern cardPattern) {
		return CardPatternOptional.ofNullable(cardPatternTreeSet.floor(cardPattern));
	}

	@Override
	public CardPattern pollFirstCardPattern() {
		return cardPatternTreeSet.pollFirst();
	}

	@Override
	public CardPattern pollLastCardPattern() {
		return cardPatternTreeSet.pollLast();
	}

	@Override
	public Iterator<CardPattern> iterator() {
		return cardPatternTreeSet.iterator();
	}
}
