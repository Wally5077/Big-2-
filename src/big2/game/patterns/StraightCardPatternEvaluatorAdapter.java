package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class StraightCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        return cardGroup.size() == 5 && cardGroup.isContinuousRank(cardPolicy);
    }

    @Override
    public StraightCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        Card[] cards = cardGroup.getCards();
        return new StraightCardPattern(policy,
                cards[0], cards[1], cards[2], cards[3], cards[4]);
    }

    @Override
    public Set<StraightCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
        return BranchOfBoundEnumerating.enumerateCardPatterns(cards, cardPolicy);
    }


    private static class BranchOfBoundEnumerating {
        private int countRecursion = 0;
        private final Card[] candidates;
        private final CardPolicy cardPolicy;
        private Set<StraightCardPattern> enumerations;
        private boolean[] member;

        public static Set<StraightCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
            return new BranchOfBoundEnumerating(cardGroup.getCards(), cardPolicy).enumerate();
        }

        public BranchOfBoundEnumerating(Card[] candidates, CardPolicy cardPolicy) {
            this.candidates = candidates;
            this.member = new boolean[candidates.length];
            this.cardPolicy = cardPolicy;
        }

        public Set<StraightCardPattern> enumerate() {
            if (enumerations == null) {
                enumerations = new HashSet<>();
                enumerating(0, getNextRank(candidates[0].getRank()),
                        1, 1, 1);
                System.out.println("Recursion: " + countRecursion);
            }
            return enumerations;
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
                    enumerations.add(collectMembersToCardPattern());
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

        private StraightCardPattern collectMembersToCardPattern() {
            Card[] cards = IntStream.range(0, member.length)
                    .filter(i -> member[i])
                    .mapToObj(i -> candidates[i]).toArray(Card[]::new);
            return new StraightCardPattern(cardPolicy, cards[0], cards[1], cards[2], cards[3], cards[4]);
        }

        private Rank getNextRank(Rank rank) {
            return cardPolicy.getNextRank(rank);
        }
    }


    public static class StraightCardPattern extends AbstractCardPattern {
        private Card card1;
        private Card card2;
        private Card card3;
        private Card card4;
        private Card card5;

        public StraightCardPattern(CardPolicy cardPolicy,
                                   Card c1, Card c2, Card c3, Card c4, Card c5) {
            super(cardPolicy, c1, c2, c3, c4, c5);
            this.card1 = c1;
            this.card2 = c2;
            this.card3 = c3;
            this.card4 = c4;
            this.card5 = c5;
        }

        @Override
        public int getLevel() {
            return cardPolicy.getLevel(card5);
        }
        public Card getCard1() {
            return card1;
        }
        public Card getCard2() {
            return card2;
        }
        public Card getCard3() {
            return card3;
        }
        public Card getCard4() {
            return card4;
        }
        public Card getCard5() {
            return card5;
        }
    }
}
