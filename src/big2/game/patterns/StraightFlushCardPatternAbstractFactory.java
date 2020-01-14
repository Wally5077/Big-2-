package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;

public class StraightFlushCardPatternAbstractFactory implements CardPatternAbstractFactory {
    @Override
    public CardPatternEvaluatorAdapter createEvaluatorAdapter() {
        return new Evaluator();
    }

    @Override
    public CardPatternPolicyAdapter createPolicyAdapter() {
        return new PolicyAdapter();
    }

    private static class Evaluator implements CardPatternEvaluatorAdapter<StraightFlushCardPattern> {
        @Override
        public Class<StraightFlushCardPattern> getCardPatternType() {
            return StraightFlushCardPattern.class;
        }

        @Override
        public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
            return cardGroup.size() == 5 && cardGroup.isContinuousRank(cardPolicy)
                    && cardGroup.allInSameSuit();
        }

        @Override
        public StraightFlushCardPattern create(CardGroup cardGroup, CardPolicy policy) {
            Card[] cards = cardGroup.getCards();
            return new StraightFlushCardPattern(policy,
                    cards[0], cards[1], cards[2], cards[3], cards[4]);
        }

        @Override
        public Set<StraightFlushCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
            return null;  //TODO
        }
    }

    private static class PolicyAdapter implements CardPatternPolicyAdapter<StraightFlushCardPattern> {
        @Override
        public Class<StraightFlushCardPattern> getCardPatternType() {
            return StraightFlushCardPattern.class;
        }

        @Override
        public int getCardPatternBaseLevel() {
            return DefaultCardPatternBaseLevels.LEVEL_STRAIGHT_FLUSH;
        }
    }

    public static class StraightFlushCardPattern extends StraightCardPatternAbstractFactory.StraightCardPattern {
        public StraightFlushCardPattern(CardPolicy cardPolicy,
                                        Card c1, Card c2, Card c3, Card c4, Card c5) {
            super(cardPolicy, c1, c2, c3, c4, c5);
        }
    }

}
