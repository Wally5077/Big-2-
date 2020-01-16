package big2.game;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternExhaustedProduct;
import big2.game.patterns.CardPatternFacade;

import java.util.Arrays;

public class HandCards extends CardGroup {
	private CardPatternFacade cardPatternFacade;

	public HandCards(CardPatternFacade cardPatternFacade, CardGroup cardGroup) {
		this(cardPatternFacade, cardGroup.getCards());
	}

	public HandCards(CardPatternFacade cardPatternFacade, Card... cards) {
		// 'sort: false' is necessary, the sorting can only be performed
		// after the cardPatternEvaluator is set, otherwise NullPointerException is raised.
		super(false, cards);
		this.cardPatternFacade = cardPatternFacade;
		sort(this.getCards());
	}

	@Override
	protected void sort(Card[] cards) {
		Arrays.sort(cards, (c1, c2) -> cardPatternFacade.getBig2Policy().compare(c1, c2));
	}

	@Override
	public HandCards selectIndices(int ...indices) {
		return new HandCards(cardPatternFacade, getCards(indices));
	}

	@Override
	public CardGroup selectRange(int startInclusive, int endExclusive) {
		return new HandCards(cardPatternFacade, super.selectRange(startInclusive, endExclusive).getCards());
	}

	@Override
	public CardGroup selectByCyclicLength(int startInclusive, int cyclicLength) {
		return new HandCards(cardPatternFacade, super.selectByCyclicLength(startInclusive, cyclicLength).getCards());
	}

	@Override
	public HandCards exclude(Card[] excludedCards) {
		return new HandCards(cardPatternFacade, super.exclude(excludedCards).getCards());
	}

	public CardPattern toPattern() {
		return cardPatternFacade.evaluate(this);
	}

	public CardPatternExhaustedProduct exhaustCardPatterns() {
		return cardPatternFacade.exhaustCardPatterns(  this);
	}

}
