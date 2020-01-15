package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPolicy;
import big2.utils.ArrayUtils;
import big2.utils.CardUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FullHouseCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        List<CardGroup> cardGroups = cardGroup.groupByRank();
        return cardGroups.size() == 2 &&
                Math.max(cardGroups.get(0).size(), cardGroups.get(1).size()) == 3 &&
                Math.min(cardGroups.get(0).size(), cardGroups.get(1).size()) == 2;
    }

    @Override
    public FullHouseCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        List<CardGroup> cardGroups = cardGroup.groupByRank();
        return new FullHouseCardPattern(policy,
                cardGroups.get(0).size() == 3 ? cardGroups.get(0).getCards() : cardGroups.get(1).getCards(),
                cardGroups.get(0).size() == 2 ? cardGroups.get(0).getCards() : cardGroups.get(1).getCards());
    }

    /**
     * For example: [A, A, A, A, 2, 2, 2, 3, 4, 4]
     * <p>
     * 1. Group By Rank and filter the size >= 2
     * Results: [[A, A, A, A], [2, 2, 2], [4, 4]]
     * <p>
     * 2. Permutation for pairs
     * Results: [[[A, A, A, A], [2, 2, 2]],
     * [[A, A, A, A], [4, 4]],
     * [[2, 2, 2], [4, 4]]]
     * <p>
     * 3. For each pair, e.g. [[A, A, A, A], [4, 4]], generate the full-house following the steps:
     * - 3.1 let each part of the pair takes turn to be the 'three cards part' or 'two cards part' (If valid).
     *  Results: (1) Three-Cards-Part: [A, A, A, A],  Two-Cards-Part: [4, 4]
     *           (2) (Invalid) Three-Cards-Part: [4, 4],  Two-Cards-Part: [A, A, A, A]
     * - 3.2 Do permutation in each part:
     *  Results: (1) Three-Cards-Part: [[A, A, A], [A, A, A], [A, A, A], [A, A, A]],  Two-Cards-Part: [[4, 4]]
     * - 3.3 Do Cartesian Product on (Three-Cards-Part, Two-Cards-Part)
     *  Results: ([A, A, A], [4, 4]), ([A, A, A], [4, 4]), ([A, A, A], [4, 4]), ([A, A, A], [4, 4])
     */
    @Override
    public Set<FullHouseCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        CardGroup[] groupByRank = cardGroup.groupByRank().stream()
                .filter(c -> c.size() >= 2).toArray(CardGroup[]::new);

        List<CardGroup[]> pairs = ArrayUtils.permutation(2, CardGroup[]::new, groupByRank)
                .stream().filter(groups -> groups[0].size() + groups[1].size() >= 5)
                .collect(Collectors.toList());

        HashSet<FullHouseCardPattern> enumeration = new HashSet<>();
        for (CardGroup[] pair : pairs) {
            Card[] part1 = pair[0].getCards();
            Card[] part2 = pair[1].getCards();
            enumeration.addAll(enumerateCardPatternsFromPair(cardPolicy, part1, part2));
            enumeration.addAll(enumerateCardPatternsFromPair(cardPolicy, part2, part1));
        }

        return enumeration;
    }

    private Set<FullHouseCardPattern> enumerateCardPatternsFromPair(CardPolicy cardPolicy, Card[] moreThanThreeCardsPart, Card[] moreThanTwoCardsPart) {
        if (moreThanThreeCardsPart.length < 3 || moreThanTwoCardsPart.length < 2) {
            return Collections.emptySet();
        }

        Card[][] threeCardsParts = ArrayUtils.permutation(3, Card[]::new, moreThanThreeCardsPart).toArray(new Card[0][]);
        Card[][] twoCardsParts = ArrayUtils.permutation(2, Card[]::new, moreThanTwoCardsPart).toArray(new Card[0][]);

        return ArrayUtils.cartesianProduct(n -> new Card[n][5], threeCardsParts, twoCardsParts)
                .stream()
                .map(fullHousePair -> new FullHouseCardPattern(cardPolicy, fullHousePair[0], fullHousePair[1]))
                .collect(Collectors.toSet());

    }

    public static class FullHouseCardPattern extends AbstractCardPattern {
        private Card[] threeCardsPart;
        private Card[] twoCardsPart;

        public FullHouseCardPattern(CardPolicy cardPolicy, Card[] threeCardsPart, Card[] twoCardsPart) {
            super(cardPolicy, threeCardsPart, twoCardsPart);
            this.threeCardsPart = threeCardsPart;
            this.twoCardsPart = twoCardsPart;
        }

        @Override
        public int getLevel() {
            return CardUtils.findMaxLevel(cardPolicy, threeCardsPart);
        }

        public Card[] getThreeCardsPart() {
            return threeCardsPart;
        }

        public Card[] getTwoCardsPart() {
            return twoCardsPart;
        }
    }

}
