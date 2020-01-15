package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;
import big2.utils.ArrayUtils;
import big2.utils.CardUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FullHouseCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        List<CardGroup> cardGroups = cardGroup.divideByRank();
        return cardGroups.size() == 2 &&
                Math.max(cardGroups.get(0).size(), cardGroups.get(1).size()) == 3 &&
                Math.min(cardGroups.get(0).size(), cardGroups.get(1).size()) == 2;
    }

    @Override
    public FullHouseCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        List<CardGroup> cardGroups = cardGroup.divideByRank();
        return new FullHouseCardPattern(policy,
                cardGroups.get(0).size() == 3 ? cardGroups.get(0).getCards() : cardGroups.get(1).getCards(),
                cardGroups.get(0).size() == 2 ? cardGroups.get(0).getCards() : cardGroups.get(1).getCards());
    }

    @Override
    public Set<FullHouseCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        CardGroup[] divideByRank = cardGroup.divideByRank().stream()
                                .filter(c -> c.size() >= 2).toArray(CardGroup[]::new);

        List<CardGroup[]> pairs = ArrayUtils.permutation(2, CardGroup[]::new, divideByRank)
                                    .stream().filter(groups -> groups[0].size() + groups[1].size() >= 5)
                                    .collect(Collectors.toList());

        HashSet<FullHouseCardPattern> enumeration = new HashSet<>();
        for (CardGroup[] pair : pairs) {
            enumeration.addAll(enumerateCardPatternsFromPair(cardPolicy, pair[0].getCards(), pair[1].getCards()));
            ArrayUtils.swap(pair, 0, 1);
            enumeration.addAll(enumerateCardPatternsFromPair(cardPolicy, pair[0].getCards(), pair[1].getCards()));
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
