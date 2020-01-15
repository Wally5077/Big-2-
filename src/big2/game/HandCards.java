package big2.game;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternExhaustion;
import big2.game.patterns.CardPatternEvaluator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class HandCards extends CardGroup {
	private CardPatternEvaluator cardPatternEvaluator;

	public HandCards(CardPatternEvaluator cardPatternEvaluator, CardGroup cardGroup) {
		this(cardPatternEvaluator, cardGroup.getCards());
	}

	public HandCards(CardPatternEvaluator cardPatternEvaluator, Card... cards) {
		// 'sort: false' is necessary, the sorting can only be performed
		// after the cardPatternEvaluator is set, otherwise NullPointerException is raised.
		super(false, cards);
		this.cardPatternEvaluator = cardPatternEvaluator;
		sort(this.getCards());
	}

	@Override
	protected void sort(Card[] cards) {
		Arrays.sort(cards, (c1, c2) -> cardPatternEvaluator.getBig2Policy().compare(c1, c2));
	}

	@Override
	public HandCards selectIndices(int ...indices) {
		return new HandCards(cardPatternEvaluator, getCards(indices));
	}

	@Override
	public CardGroup selectRange(int startInclusive, int endExclusive) {
		return new HandCards(cardPatternEvaluator, super.selectRange(startInclusive, endExclusive).getCards());
	}

	@Override
	public CardGroup selectByCyclicLength(int startInclusive, int cyclicLength) {
		return new HandCards(cardPatternEvaluator, super.selectByCyclicLength(startInclusive, cyclicLength).getCards());
	}

	@Override
	public HandCards exclude(Card[] excludedCards) {
		return new HandCards(cardPatternEvaluator, super.exclude(excludedCards).getCards());
	}


	public CardPattern toPattern() {
		return cardPatternEvaluator.evaluate(this);
	}

	public CardPatternExhaustion exhaustCardPatterns() {
		return cardPatternEvaluator.enumerateCardPatterns(  this);
	}

}
