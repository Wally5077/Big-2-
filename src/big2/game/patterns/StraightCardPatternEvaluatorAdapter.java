package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

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
        return null;  //TODO
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
