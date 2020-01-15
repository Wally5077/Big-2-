package big2.game.patterns;

import big2.game.AbstractCardPatternExhaustion;
import big2.game.policies.CardPatternPolicy;

import java.util.*;

//TODO
public class LazyCardPatternExhaustion extends AbstractCardPatternExhaustion {

	public LazyCardPatternExhaustion(List<CardPattern> cardPatterns, CardPatternPolicy cardPatternPolicy) {
		super(cardPatterns, cardPatternPolicy);
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
		return null;
	}

	@Override
	public CardPattern higherCardPattern(CardPattern cardPattern) {
		return null;
	}

	@Override
	public CardPattern ceilingCardPattern(CardPattern cardPattern) {
		return null;
	}

	@Override
	public CardPattern floorCardPattern(CardPattern cardPattern) {
		return null;
	}

	@Override
	public CardPattern pollFirstCardPattern() {
		return null;
	}

	@Override
	public CardPattern pollLastCardPattern() {
		return null;
	}

}
