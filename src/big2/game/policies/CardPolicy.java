package big2.game.policies;

import big2.cards.Card;
import big2.cards.Rank;

public interface CardPolicy {
	 int getLevel(Card card);
	 Rank getNextRank(Rank rank);
}
