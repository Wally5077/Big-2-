package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.patterns.StraightCardPatternAdapter.StraightCardPattern;
import big2.game.policies.CardPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class StraightBranchAndBoundExhausting {
    private int recursionCount = 0;
    private final Card[] candidates;
    private final CardPolicy cardPolicy;
    private Set<StraightCardPattern> product;
    private boolean[] member;

    public static Set<StraightCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        cardGroup.sortIfNotSorted();
        return new StraightBranchAndBoundExhausting(cardGroup.getCards(), cardPolicy).enumerate();
    }

    private StraightBranchAndBoundExhausting(Card[] candidates, CardPolicy cardPolicy) {
        this.candidates = candidates;
        this.cardPolicy = cardPolicy;
    }

    private Set<StraightCardPattern> enumerate() {
        product = new HashSet<>();
        member = new boolean[candidates.length];
        for (int i = 0; i < candidates.length; i++) {
            member[i] = true;
            enumerating(getNextRank(candidates[i].getRank()), 1, (i + 1) % candidates.length);
            member[i] = false;
        }

        return product;
    }

    private int getRecursionCount() {
        return recursionCount;
    }

    /**
     * Note: This algorithm assumes the candidate Cards are sort.
     *
     * @param expectNextRank the rank expected in the sequence of Straight being searched
     * @param curLength      number of members have been found in the sequence of Straight being searched
     * @param curIndex       current index of the card being seeked
     */
    private void enumerating(Rank expectNextRank, int curLength, int curIndex) {
        recursionCount ++;

        Card card = candidates[curIndex];
        if (card.getRank() == expectNextRank) {
            member[curIndex] = true;
            if (curLength == 4) {
                product.add(collectMembersToCardPattern());
            } else { // keep finding the next Flush member
                enumerating(getNextRank(expectNextRank), curLength + 1, (curIndex + 1) % candidates.length /*cyclic seeking*/);
            }
            member[curIndex] = false;
            enumerating(expectNextRank, curLength, (curIndex + 1) % candidates.length);
        } else if (getNextRank(card.getRank()) == expectNextRank) {
            enumerating(expectNextRank,
                    curLength,
                    (curIndex + 1) % candidates.length);
        }
    }

    private StraightCardPattern collectMembersToCardPattern() {
        Card[] cards = IntStream.range(0, candidates.length)
                .filter(i -> member[i])
                .mapToObj(i -> candidates[i]).toArray(Card[]::new);
        try {
            return new StraightCardPattern(cardPolicy, cards[0], cards[1], cards[2], cards[3], cards[4]);
        } catch (ArrayIndexOutOfBoundsException err) {
            return null;
        }

    }

    private Rank getNextRank(Rank rank) {
        return cardPolicy.getNextRank(rank);
    }

}
