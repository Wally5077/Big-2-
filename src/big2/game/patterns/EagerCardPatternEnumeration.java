package big2.game.patterns;

import big2.game.AbstractCardPatternEnumeration;
import big2.game.patterns.CardPattern;
import big2.game.policies.CardPatternPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class EagerCardPatternEnumeration extends AbstractCardPatternEnumeration {
	private Map<Class<? extends CardPattern>, List<CardPattern>> cardPatternMap = new HashMap<>();
	private TreeSet<CardPattern> cardPatternTreeSet = new TreeSet<>();

	public EagerCardPatternEnumeration(List<CardPattern> cardPatterns, CardPatternPolicy cardPatternPolicy) {
		super(cardPatterns, cardPatternPolicy);
		cardPatternTreeSet.addAll(cardPatterns);
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
	public CardPattern lowerCardPattern(CardPattern cardPattern) {
		return cardPatternTreeSet.lower(cardPattern);
	}

	@Override
	public CardPattern higherCardPattern(CardPattern cardPattern) {
		return cardPatternTreeSet.higher(cardPattern);
	}

	@Override
	public CardPattern ceilingCardPattern(CardPattern cardPattern) {
		return cardPatternTreeSet.ceiling(cardPattern);
	}

	@Override
	public CardPattern floorCardPattern(CardPattern cardPattern) {
		return cardPatternTreeSet.floor(cardPattern);
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
