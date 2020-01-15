package big2.game.patterns;

import big2.game.AbstractCardPatternExhaustion;
import big2.game.policies.CardPatternPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class EagerCardPatternExhaustion extends AbstractCardPatternExhaustion {
	private Map<Class<? extends CardPattern>, List<CardPattern>> cardPatternMap = new HashMap<>();
	private TreeSet<CardPattern> cardPatternTreeSet;

	public EagerCardPatternExhaustion(List<CardPattern> cardPatterns, CardPatternPolicy cardPatternPolicy) {
		super(cardPatterns, cardPatternPolicy);
		cardPatternTreeSet = new TreeSet<>(cardPatternPolicy::compare);
		cardPatternTreeSet.addAll(cardPatterns);  //TODO TreeSet's equality issue with CardPolicy's level
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
