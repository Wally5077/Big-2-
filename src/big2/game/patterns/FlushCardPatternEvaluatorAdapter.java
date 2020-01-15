package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class FlushCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {
    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        return cardGroup.size() == 5 && cardGroup.allInSameSuit();
    }

    @Override
    public FlushCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        return new FlushCardPattern(policy, cardGroup.getCards());
    }

    @Override
    public Set<FlushCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        boolean[] member = new boolean[cardGroup.size()];
        member[0] = true;
        return branchOfBoundEnumerating(new HashSet<>(), cardGroup.getCards(),
                member, cardPolicy,
                0, cardPolicy.getNextRank(cardGroup.get(0).getRank()),
                1, 1, 1);  //TODO
    }

    // TODO very ugly
    private Set<FlushCardPattern> branchOfBoundEnumerating(Set<FlushCardPattern> enumerations,
                                                           Card[] candidates, boolean[] member,
                                                           CardPolicy cardPolicy, int startIndex,
                                                           Rank expectNextRank, int curLength,
                                                           int curSeeking, int curIndex) {
        Card card = candidates[curIndex];
        if (card.getRank() == expectNextRank) {
            member[curIndex] = true;
            if (curLength == 4) {
                enumerations.add(new FlushCardPattern(cardPolicy,
                        IntStream.range(0, member.length)
                                .filter(i -> member[i])
                                .mapToObj(i -> candidates[i]).toArray(Card[]::new)));
            } else { // keep finding the next Flush member
                branchOfBoundEnumerating(enumerations, candidates, member, cardPolicy,
                        startIndex, cardPolicy.getNextRank(expectNextRank),
                        curLength + 1, curSeeking + 1,
                        (curIndex + 1) % candidates.length /*cyclic seeking*/);
            }
            member[curIndex] = false;
            branchOfBoundEnumerating(enumerations, candidates, member,cardPolicy,
                    startIndex, expectNextRank, curLength,
                    curSeeking+1, (curIndex+1)%candidates.length);
        } else if (curSeeking < candidates.length){
            branchOfBoundEnumerating(enumerations, candidates, member, cardPolicy,
                    startIndex, expectNextRank,
                    curLength, curSeeking + 1,
                    (curIndex + 1) % candidates.length /*cyclic seeking*/);
        }

        // The end for the finding started from startIndex, switch to new branch
        if ((curSeeking >= candidates.length ) &&
                startIndex != candidates.length - 1) {
            int nextStartIndex = startIndex + 1;
            boolean[] nextMember = new boolean[candidates.length];
            nextMember[nextStartIndex] = true;
            branchOfBoundEnumerating(enumerations, candidates, nextMember,
                    cardPolicy, nextStartIndex,
                    cardPolicy.getNextRank(candidates[nextStartIndex % candidates.length].getRank()),
                    1, 1, (nextStartIndex+1)%candidates.length);
        }

        return enumerations;
    }

    private static class BranchOfBoundEnumerating {
        private Set<FlushCardPattern> enumerations;
        private Card[] candidates;
        private boolean[] member;
        private CardPolicy cardPolicy;

        public BranchOfBoundEnumerating(Card[] candidates, CardPolicy cardPolicy) {
            this.candidates = candidates;
            this.member = new boolean[candidates.length];
            this.cardPolicy = cardPolicy;
        }

        public static Set<FlushCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
            return new BranchOfBoundEnumerating(cardGroup.getCards(), cardPolicy).enumerate();
        }

        public Set<FlushCardPattern> enumerate() {
            if (enumerations == null) {
                enumerations = new HashSet<>();
                member[0] = true;
                enumerating(0, getNextRank(candidates[0].getRank()),
                        1, 1, 1);
            }
            return enumerations;
        }

        private void enumerating(int startIndex, Rank expectNextRank, int curLength,
                                 int curSeeking, int curIndex) {
            boolean newEnumerationFound = false;
            Card card = candidates[curIndex];
            if (card.getRank() == expectNextRank) {
                member[curIndex] = true; // pick matched card
                if (curLength+1 == 4) { // pick and complete a new pattern
                    enumerations.add(collectMembersToCardPattern());
                    newEnumerationFound = true;
                } else { // pick and keep finding the next Flush member
                    enumerating(startIndex, getNextRank(expectNextRank),
                            curLength + 1, curSeeking + 1,
                            (curIndex + 1) % candidates.length /*cyclic seeking*/);
                }
                member[curIndex] = false; // don't pick matched card
                enumerating(startIndex, expectNextRank, curLength,
                        curSeeking+1, (curIndex+1)%candidates.length);
            } else if (curSeeking < candidates.length) { // don't match, cyclic seeking the next Flush member
                enumerating(startIndex, expectNextRank,
                        curLength, curSeeking + 1,
                        (curIndex + 1) % candidates.length);
            }

            // The end for one round seeking, start the new search from the next startIndex
            if ((curSeeking >= candidates.length) &&
                    startIndex != candidates.length - 1) {
                int nextStartIndex = startIndex + 1;
                member[startIndex] = false;
                member[nextStartIndex] = true;
                enumerating(nextStartIndex,
                        getNextRank(candidates[nextStartIndex % candidates.length].getRank()),
                        1, 1, (nextStartIndex + 1) % candidates.length);
            }
        }

        private FlushCardPattern collectMembersToCardPattern() {
            return new FlushCardPattern(cardPolicy, IntStream.range(0, member.length)
                            .filter(i -> member[i])
                            .mapToObj(i -> candidates[i]).toArray(Card[]::new));
        }

        private Rank getNextRank(Rank rank) {
            return cardPolicy.getNextRank(rank);
        }
    }

    public static class FlushCardPattern extends AbstractCardPattern {
        private int level;

        public FlushCardPattern(CardPolicy cardPolicy, Card... cards) {
            super(cardPolicy, cards);
            assert cards.length == 5 : "the length of a flush should be 5, given " + cards.length;
            this.level = CardUtils.findMaxLevel(cardPolicy, cards);
        }

        @Override
        public int getLevel() {
            return level;
        }
    }

}
