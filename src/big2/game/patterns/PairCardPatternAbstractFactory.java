package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

public class PairCardPatternAbstractFactory implements CardPatternAbstractFactory {
    @Override
    public CardPatternEvaluatorAdapter createEvaluatorAdapter() {
        return new Evaluator();
    }

    @Override
    public CardPatternPolicyAdapter createPolicyAdapter() {
        return new PolicyAdapter();
    }


    private static class Evaluator implements CardPatternEvaluatorAdapter<PairCardPattern> {
        @Override
        public Class<PairCardPattern> getCardPatternType() {
            return PairCardPattern.class;
        }

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
        public Set<PairCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
            return null;  //TODO
        }
    }

    private static class PolicyAdapter implements CardPatternPolicyAdapter<PairCardPattern> {
        @Override
        public Class<PairCardPattern> getCardPatternType() {
            return PairCardPattern.class;
        }

        @Override
        public int getCardPatternBaseLevel() {
            return DefaultCardPatternBaseLevels.LEVEL_PAIR;
        }
    }


    public static class PairCardPattern extends AbstractCardPattern {
        private Card card1;
        private Card card2;
        public PairCardPattern(CardPolicy cardPolicy, Card c1, Card c2) {
            super(cardPolicy, c1, c2);
            this.card1 = c1;
            this.card2 = c2;
        }

        @Override
        public int getLevel() {
            return Math.max(cardPolicy.getLevel(card1), cardPolicy.getLevel(card2));
        }

        public Card getCard1() {
            return card1;
        }

        public Card getCard2() {
            return card2;
        }
    }
}
