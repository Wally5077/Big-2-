package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.Set;

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
    public Set<CardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
        return null;  //TODO
    }

    public static class FlushCardPattern extends AbstractCardPattern {
        public FlushCardPattern(CardPolicy cardPolicy, Card... cards) {
            super(cardPolicy, cards);
        }
        @Override
        public int getLevel() {
            return CardUtils.findMaxLevel(cardPolicy, cards);
        }
    }

}
