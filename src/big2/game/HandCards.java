package big2.game;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternEnumeration;
import big2.game.patterns.CardPatternEvaluator;

public class HandCards extends CardGroup {
	private CardPatternEvaluator cardPatternEvaluator;

	public HandCards(CardPatternEvaluator cardPatternEvaluator, CardGroup cardGroup) {
		this(cardPatternEvaluator, cardGroup.getCards());
	}

	public HandCards(CardPatternEvaluator cardPatternEvaluator, Card... cards) {
		super(cards);
		this.cardPatternEvaluator = cardPatternEvaluator;
	}

	@Override
	public HandCards selectIndices(int ...indices) {
		return new HandCards(cardPatternEvaluator, getCards(indices));
	}

	@Override
	public HandCards remove(Card[] removedCards) {
		return new HandCards(cardPatternEvaluator, super.remove(removedCards).getCards());
	}

	public CardPattern toPattern() {
		return cardPatternEvaluator.evaluate(this);
	}

	public CardPatternEnumeration enumerateCardPatterns() {
		return cardPatternEvaluator.enumerateCardPatterns(  this);
	}

}
