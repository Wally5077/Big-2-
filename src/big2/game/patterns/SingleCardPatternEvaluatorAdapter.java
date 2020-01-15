package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPolicy;

import java.util.Set;
import java.util.stream.Collectors;

public class SingleCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        return cardGroup.size() == 1;
    }

    @Override
    public SingleCardPattern create(CardGroup cardGroup, CardPolicy cardPolicy) {
        return new SingleCardPattern(cardPolicy, cardGroup.get(0));
    }

    @Override
    public Set<SingleCardPattern> exhaustCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
        return cards.stream()
                .map(card -> new SingleCardPattern(cardPolicy, card))
                .collect(Collectors.toSet());
    }

    public static class SingleCardPattern extends AbstractCardPattern {
        private Card card;
        private int level;

        public SingleCardPattern(CardPolicy cardPolicy, Card card) {
            super(cardPolicy, card);
            this.cardPolicy = cardPolicy;
            this.card = card;
            this.level = cardPolicy.getLevel(card);
        }

        public Card getCard() {
            return card;
        }

        @Override
        public int getLevel() {
            return level;
        }
    }
}
