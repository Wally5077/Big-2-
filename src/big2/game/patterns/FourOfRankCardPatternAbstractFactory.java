package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.Set;

public class FourOfRankCardPatternAbstractFactory implements
        CardPatternAbstractFactory<FourOfRankCardPatternAbstractFactory.FourOfRankCardPattern> {
    @Override
    public CardPatternEvaluatorAdapter<FourOfRankCardPattern> createEvaluatorAdapter() {
        return new Evaluator();
    }

    @Override
    public CardPatternPolicyAdapter<FourOfRankCardPattern> createPolicyAdapter() {
        return new PolicyAdapter();
    }

    private static class Evaluator implements CardPatternEvaluatorAdapter<FourOfRankCardPattern>  {
        @Override
        public Class<FourOfRankCardPattern> getCardPatternType() {
            return FourOfRankCardPattern.class;
        }

        @Override
        public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
            return cardGroup.size() == 4 && cardGroup.allInSameRank();
        }

        @Override
        public FourOfRankCardPattern create(CardGroup cardGroup, CardPolicy policy) {
            return new FourOfRankCardPattern(policy, cardGroup.getCards());
        }

        @Override
        public Set<FourOfRankCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
            return null;  //TODO
        }
    }

    private static class PolicyAdapter implements CardPatternPolicyAdapter<FourOfRankCardPattern> {
        @Override
        public Class<FourOfRankCardPattern> getCardPatternType() {
            return FourOfRankCardPattern.class;
        }
        @Override
        public int getCardPatternBaseLevel() {
            return DefaultCardPatternBaseLevels.LEVEL_FLUSH;
        }
    }


    public static class FourOfRankCardPattern extends AbstractCardPattern {
        public FourOfRankCardPattern(CardPolicy cardPolicy, Card... cards) {
            super(cardPolicy, cards);
        }

        @Override
        public int getLevel() {
            return CardUtils.findMaxLevel(cardPolicy, cards);
        }
    }

}
