package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPolicy;
import big2.utils.ArrayUtils;
import big2.utils.CardUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Set<FlushCardPattern> exhaustCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        List<CardGroup> groupBySuit = cardGroup.groupBySuit().stream()
                                .filter(c -> c.size() >= 5).collect(Collectors.toList());

        HashSet<FlushCardPattern> patterns = new HashSet<>();
        for (CardGroup group : groupBySuit) {
            List<Card[]> combination = ArrayUtils.combination(5, Card[]::new, group.getCards());
            for (Card[] cards : combination) {
                patterns.add(new FlushCardPattern(cardPolicy, cards));
            }
        }
        return patterns;
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
