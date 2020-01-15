package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntUnaryOperator;
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
        return null; //TODO
    }


    public static class FlushCardPattern extends AbstractCardPattern {
        private int level;

        public FlushCardPattern(CardPolicy cardPolicy, Card... cards) {
            super(cardPolicy, cards);
            this.level = CardUtils.findMaxLevel(cardPolicy, cards);
        }

        @Override
        public int getLevel() {
            return level;
        }
    }

}
