package big2.game.policies;

import big2.cards.Card;
import big2.cards.Rank;

public interface CardPolicy {
	 int getLevel(Card card);

	 default int compareCards(Card card1, Card card2) {
	 	return getLevel(card1) - getLevel(card2);
	 }

	 Rank getNextRank(Rank rank);
}
