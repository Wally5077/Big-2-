package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPolicy;
import big2.utils.ArrayUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PairCardPatternAdapter implements CardPatternAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        return cardGroup.size() == 2 && cardGroup.allInSameRank();
    }

    @Override
    public PairCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        Card[] cards = cardGroup.getCards(0, 1);
        return new PairCardPattern(policy, cards[0], cards[1]);
    }

    @Override
    public Set<PairCardPattern> exhaustCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
        HashSet<PairCardPattern> pairEnumeration = new HashSet<>();
        List<CardGroup> groupByRank = cards.groupByRank();
        for (CardGroup cardGroup : groupByRank) {
            List<Card[]> pairs = ArrayUtils.combination(2, Card[]::new, cardGroup.getCards());
            for (Card[] pair : pairs) {
                pairEnumeration.add(new PairCardPattern(cardPolicy, pair[0], pair[1]));
            }
        }
        return pairEnumeration;
    }


    public static class PairCardPattern extends AbstractCardPattern {
        private Card card1;
        private Card card2;
        private int level;
        public PairCardPattern(CardPolicy cardPolicy, Card c1, Card c2) {
            super(cardPolicy, c1, c2);
            this.card1 = c1;
            this.card2 = c2;
            this.level = Math.max(cardPolicy.getLevel(card1), cardPolicy.getLevel(card2));
        }

        @Override
        public int getLevel() {
            return level;
        }

        public Card getCard1() {
            return card1;
        }

        public Card getCard2() {
            return card2;
        }
    }
}
