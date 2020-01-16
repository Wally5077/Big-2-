package big2.game.patterns;

import big2.cards.CardGroup;
import big2.game.policies.Big2Policy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EagerCardPatternExhaustion implements CardPatternExhaustion {
	private Collection<CardPatternEvaluatorAdapter> evaluatorAdapters;
	private Map<Class<? extends CardPattern>, List<CardPattern>> cardPatternMap;
	private TreeSet<CardPattern> cardPatternTreeSet;

	public EagerCardPatternExhaustion(Collection<CardPatternEvaluatorAdapter> evaluatorAdapters,
									  CardGroup srcCardGroup,
									  Big2Policy big2Policy) {
		this.evaluatorAdapters = evaluatorAdapters;
		cardPatternTreeSet = new TreeSet<>(big2Policy::compare);
		cardPatternTreeSet.addAll(exhaustion(srcCardGroup, big2Policy));
		cardPatternMap = cardPatternTreeSet.stream().collect(Collectors.groupingBy(CardPattern::getClass));
	}

	private List<CardPattern> exhaustion(CardGroup srcCardGroup, Big2Policy big2Policy) {
		return evaluatorAdapters.stream()
				.flatMap(adapter -> adapter.exhaustCardPatterns(srcCardGroup, big2Policy).stream())
				.collect(Collectors.toList());
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

}
