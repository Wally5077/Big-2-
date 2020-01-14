package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;

import java.util.Set;
import java.util.stream.Collectors;

public class SingleCardPatternAbstractFactory implements CardPatternAbstractFactory {

    @Override
    public CardPatternEvaluatorAdapter createEvaluatorAdapter() {
        return new Evaluator();
    }

    @Override
    public CardPatternPolicyAdapter createPolicyAdapter() {
        return new PolicyAdapter();
    }


    private static class Evaluator implements CardPatternEvaluatorAdapter<SingleCardPattern> {
        @Override
        public Class<SingleCardPattern> getCardPatternType() {
            return SingleCardPattern.class;
        }

        @Override
        public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
            return cardGroup.size() == 1;
        }

        @Override
        public SingleCardPattern create(CardGroup cardGroup, CardPolicy cardPolicy) {
            return new SingleCardPattern(cardPolicy, cardGroup.get(0));
        }

        @Override
        public Set<SingleCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
            return cards.stream()
                    .map(card -> new SingleCardPattern(cardPolicy, card))
                    .collect(Collectors.toSet());
        }
    }

    private static class PolicyAdapter implements CardPatternPolicyAdapter<SingleCardPattern> {
        @Override
        public Class<SingleCardPattern> getCardPatternType() {
            return SingleCardPattern.class;
        }

        @Override
        public int getCardPatternBaseLevel() {
            return DefaultCardPatternBaseLevels.LEVEL_SINGLE;
        }

        @Override
        public int getTotalLevel(CardPattern cardPattern) {
            return cardPattern.getLevel();
        }
    }

    public static class SingleCardPattern extends AbstractCardPattern {
        private Card card;

        public SingleCardPattern(CardPolicy cardPolicy, Card card) {
            super(cardPolicy, card);
            this.cardPolicy = cardPolicy;
            this.card = card;
        }

        public Card getCard() {
            return card;
        }

        @Override
        public int getLevel() {
            return cardPolicy.getLevel(card);
        }
    }
}
