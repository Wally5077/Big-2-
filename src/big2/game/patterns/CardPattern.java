package big2.game.patterns;

import big2.cards.Card;

public interface CardPattern extends Comparable<CardPattern> {
	int getLevel();
	Card[] getCards();
}
