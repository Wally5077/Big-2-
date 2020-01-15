package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.policies.CardPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class StraightBranchOfBoundExhaustion {
    private int countRecursion = 0;
    private final Card[] candidates;
    private final CardPolicy cardPolicy;
    private Set<StraightCardPatternEvaluatorAdapter.StraightCardPattern> exhaustion;
    private boolean[] member;

    public static Set<StraightCardPatternEvaluatorAdapter.StraightCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        cardGroup.sortIfNotSorted();
        return new StraightBranchOfBoundExhaustion(cardGroup.getCards(), cardPolicy).enumerate();
    }

    public StraightBranchOfBoundExhaustion(Card[] candidates, CardPolicy cardPolicy) {
        this.candidates = candidates;
        this.member = new boolean[candidates.length];
        this.cardPolicy = cardPolicy;
    }

    public Set<StraightCardPatternEvaluatorAdapter.StraightCardPattern> enumerate() {
        if (exhaustion == null) {
            exhaustion = new HashSet<>();
            enumerating(0, getNextRank(candidates[0].getRank()),
                    1, 1, 1);
            System.out.println("Recursion: " + countRecursion);
        }
        return exhaustion;
    }

    /**
     * Note: This algorithm assumes the candidate Cards are sorted.
     *
     * @param startIndex where the Flush pattern starts
     * @param expectNextRank the rank expected in the sequence of Flush being searched
     * @param curLength number of members have been found in the sequence of Flush being searched
     * @param curSeeking count of seeking in the current sequence
     * @param curIndex current index of the card being seeked
     */
    private void enumerating(int startIndex, Rank expectNextRank, int curLength,
                             int curSeeking, int curIndex) {
        member[startIndex] = true;
        countRecursion ++;

        Card card = candidates[curIndex];
        if (card.getRank() == expectNextRank) {
            member[curIndex] = true;
            if (curLength == 4) {
                exhaustion.add(collectMembersToCardPattern());
            } else { // keep finding the next Flush member
                enumerating(startIndex, getNextRank(expectNextRank),
                        curLength + 1, curSeeking + 1,
                        (curIndex + 1) % candidates.length /*cyclic seeking*/);
            }
            member[curIndex] = false;
            enumerating(startIndex, expectNextRank, curLength,
                    curSeeking+1, (curIndex+1)%candidates.length);
        } else if (curSeeking < candidates.length){
            enumerating(startIndex, expectNextRank,
                    curLength, curSeeking + 1,
                    (curIndex + 1) % candidates.length /*cyclic seeking*/);
        }


        // The end for the finding started from startIndex, switch to new branch
        if ((curSeeking >= candidates.length) &&
                startIndex != candidates.length - 1) {
            int nextStartIndex = startIndex + 1;
            resetMember();
            enumerating(nextStartIndex,
                    getNextRank(candidates[nextStartIndex % candidates.length].getRank()),
                    1, 1, (nextStartIndex+1)%candidates.length);
        }
    }

    private void resetMember() {
        for (int i = 0; i < member.length; i++) {
            member[i] = false;
        }
    }

    private StraightCardPatternEvaluatorAdapter.StraightCardPattern collectMembersToCardPattern() {
        Card[] cards = IntStream.range(0, member.length)
                .filter(i -> member[i])
                .mapToObj(i -> candidates[i]).toArray(Card[]::new);
        return new StraightCardPatternEvaluatorAdapter.StraightCardPattern(cardPolicy, cards[0], cards[1], cards[2], cards[3], cards[4]);
    }

    private Rank getNextRank(Rank rank) {
        return cardPolicy.getNextRank(rank);
    }
}
