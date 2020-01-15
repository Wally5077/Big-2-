package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FourOfRankCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {
    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        if (cardGroup.size() != 5) {
            return false;
        }

        List<CardGroup> cardGroups = cardGroup.groupByRank();
        return cardGroups.size() == 2 &&
                Math.max(cardGroups.get(0).size(), cardGroups.get(1).size()) == 4 &&
                Math.min(cardGroups.get(0).size(), cardGroups.get(1).size()) == 1;
    }

    @Override
    public FourOfRankCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        List<CardGroup> cardGroups = cardGroup.groupByRank();
        return new FourOfRankCardPattern(policy,
                cardGroups.get(0).size() == 4 ? cardGroups.get(0).getCards(): cardGroups.get(1).getCards(),
                cardGroups.get(0).size() == 1 ? cardGroups.get(0).get(0): cardGroups.get(1).get(0));
    }

    @Override
    public Set<FourOfRankCardPattern> enumerateCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        List<CardGroup> fourOfRanks = cardGroup.groupByRank().stream()
                            .filter(c -> c.size() == 4).collect(Collectors.toList());

        HashSet<FourOfRankCardPattern> patterns = new HashSet<>();
        for (CardGroup fourOfRank : fourOfRanks) {
            Rank rank = fourOfRank.get(0).getRank();
            for (Card card : cardGroup) {
                if (rank != card.getRank()) {
                    patterns.add(new FourOfRankCardPattern(cardPolicy,
                            fourOfRank.getCards(), card));
                }
            }
        }
        return patterns;
    }

    public static class FourOfRankCardPattern extends AbstractCardPattern {
        private Card[] fourCardsPart;
        private Card foot;

        public FourOfRankCardPattern(CardPolicy cardPolicy, Card[] fourCardsPart, Card foot) {
            super(cardPolicy, fourCardsPart, new Card[]{foot});
            this.fourCardsPart = fourCardsPart;
            this.foot = foot;
        }

        @Override
        public int getLevel() {
            return CardUtils.findMaxLevel(cardPolicy, cards);
        }

        public Card[] getFourCardsPart() {
            return fourCardsPart;
        }

        public void setFourCardsPart(Card[] fourCardsPart) {
            this.fourCardsPart = fourCardsPart;
        }

        public Card getFoot() {
            return foot;
        }

        public void setFoot(Card foot) {
            this.foot = foot;
        }
    }

}
