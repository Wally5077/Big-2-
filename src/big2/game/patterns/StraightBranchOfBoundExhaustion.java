package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.patterns.StraightCardPatternEvaluatorAdapter.StraightCardPattern;
import big2.game.policies.CardPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class StraightBranchOfBoundExhaustion {
    private int recursionCount = 0;
    private final Card[] candidates;
    private final CardPolicy cardPolicy;
    private Set<StraightCardPattern> exhaustion;
    private boolean[] member;

    public static Set<StraightCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        cardGroup.sortIfNotSorted();
        return new StraightBranchOfBoundExhaustion(cardGroup.getCards(), cardPolicy).enumerate();
    }

    public StraightBranchOfBoundExhaustion(Card[] candidates, CardPolicy cardPolicy) {
        this.candidates = candidates;
        this.member = new boolean[candidates.length];
        this.cardPolicy = cardPolicy;
    }

    public Set<StraightCardPattern> enumerate() {
        if (exhaustion == null) {
            exhaustion = new HashSet<>();
            enumerating(0, getNextRank(candidates[0].getRank()),
                    1, 1, 1);
        }
        return exhaustion;
    }

    public int getRecursionCount() {
        return recursionCount;
    }

    /**
     * Note: This algorithm assumes the candidate Cards are sort.
     *
     * @param startIndex     where the Flush pattern starts
     * @param expectNextRank the rank expected in the sequence of Flush being searched
     * @param curLength      number of members have been found in the sequence of Flush being searched
     * @param curSeeking     count of seeking in the current sequence
     * @param curIndex       current index of the card being seeked
     */
    private void enumerating(int startIndex, Rank expectNextRank, int curLength,
                             int curSeeking, int curIndex) {
        recursionCount++;
        member[startIndex] = true;

        Card card = candidates[curIndex];
        if (card.getRank() == expectNextRank) {
            member[curIndex] = true;
            if (curLength == 4) {
                StraightCardPattern pattern = collectMembersToCardPattern();
                assert !exhaustion.contains(pattern);
                exhaustion.add(pattern);
            } else { // keep finding the next Flush member
                enumerating(startIndex, getNextRank(expectNextRank),
                        curLength + 1, curSeeking + 1,
                        (curIndex + 1) % candidates.length /*cyclic seeking*/);
            }
            member[curIndex] = false;
            enumerating(startIndex, expectNextRank, curLength,
                    curSeeking + 1, (curIndex + 1) % candidates.length);
        } else if (getNextRank(card.getRank()) == expectNextRank) {
            enumerating(startIndex, expectNextRank,
                    curLength, curSeeking + 1,
                    (curIndex + 1) % candidates.length);
        }



        // The end for the finding started from startIndex, switch to new branch
        if (startIndex == curIndex-1 &&
                startIndex != candidates.length - 1) {
            int nextStartIndex = startIndex + 1;
            member[startIndex] = false;
            enumerating(nextStartIndex,
                    getNextRank(candidates[nextStartIndex % candidates.length].getRank()),
                    1, 1, (nextStartIndex + 1) % candidates.length);
        }
    }

    private void resetMember() {
        for (int i = 0; i < member.length; i++) {
            member[i] = false;
        }
    }

    private StraightCardPattern collectMembersToCardPattern() {
        Card[] cards = IntStream.range(0, member.length)
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
