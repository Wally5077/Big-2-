package big2.game.policies;

import big2.cards.Card;
import big2.cards.Rank;
import big2.cards.Suit;

import static big2.cards.Rank.*;

public class StandardCardPolicy implements CardPolicy {
    private static Rank[] sortedRanks = {R3, R4, R5, R6, R7, R8, R9, R10, J, Q, K, A, R2};

    @Override
    public int getLevel(Card card) {
        return getRankLevel(card.getRank()) * 10 + getSuitLevel(card.getSuit());
    }

    @Override
    public Rank getNextRank(Rank rank) {
        for (int i = 0; i < sortedRanks.length; i++) {
            if (sortedRanks[i] == rank) {
                return i == sortedRanks.length-1 ? sortedRanks[0] : sortedRanks[i+1];
            }
        }
        throw new RuntimeException("The rank " + rank + " is not found.");
    }

    private int getSuitLevel(Suit suit) {
        switch (suit) {
            case CLUB:
                return 0;
            case DIAMOND:
                return 1;
            case HEART:
                return 2;
            case SPADE:
                return 3;
            default:
                throw new IllegalArgumentException("Suit " + suit + " not found.");
        }
    }

    private int getRankLevel(Rank rank) {
        switch (rank) {
            case R3:
                return 3;
            case R4:
                return 4;
            case R5:
                return 5;
            case R6:
                return 6;
            case R7:
                return 7;
            case R8:
                return 8;
            case R9:
                return 9;
            case R10:
                return 10;
            case J:
                return 11;
            case Q:
                return 12;
            case K:
                return 13;
            case A:
                return 14;
            case R2:
                return 15;
            default:
                throw new IllegalArgumentException("Rank " + rank + " not found.");
        }
    }
}
