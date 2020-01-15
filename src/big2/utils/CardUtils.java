package big2.utils;

import big2.cards.Card;
import big2.cards.Rank;
import big2.cards.Suit;
import big2.game.policies.CardPolicy;

public class CardUtils {


    public static int findMaxLevel(CardPolicy cardPolicy, Card ...cards) {
        int max = -99999;
        for (Card card : cards) {
            int level = cardPolicy.getLevel(card);
            if (level > max) {
                max = level;
            }
        }
        return max;
    }

    public static boolean sameRanks(Card ...cards) {
        if (cards.length == 0)
            return true;

        Rank rank = cards[0].getRank();
        for (int i = 1; i < cards.length; i++) {
            if (rank != cards[i].getRank())
                return false;
        }
        return true;
    }

    public static boolean sameSuits(Card ...cards) {
        if (cards.length == 0)
            return true;

        Suit suit = cards[0].getSuit();
        for (int i = 1; i < cards.length; i++) {
            if (suit != cards[i].getSuit())
                return false;
        }
        return true;
    }

}
