package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.StraightCardPatternAdapter.StraightCardPattern;
import big2.game.policies.CardPolicy;

import java.util.Set;
import java.util.stream.Collectors;

public class StraightFlushCardPatternAdapter implements CardPatternAdapter {
    private FlushCardPatternAdapter flushEvaluator;
    private StraightCardPatternAdapter straightEvaluator;

    public StraightFlushCardPatternAdapter(FlushCardPatternAdapter flushEvaluator,
                                           StraightCardPatternAdapter straightEvaluator) {
        this.flushEvaluator = flushEvaluator;
        this.straightEvaluator = straightEvaluator;
    }

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        return flushEvaluator.isMatched(cardGroup, cardPolicy) &&
                straightEvaluator.isMatched(cardGroup, cardPolicy);
    }

    @Override
    public StraightFlushCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        Card[] cards = cardGroup.getCards();
        return new StraightFlushCardPattern(policy,
                cards[0], cards[1], cards[2], cards[3], cards[4]);
    }

    @Override
    public Set<StraightFlushCardPattern> exhaustCardPatterns(CardGroup cardGroup, CardPolicy cardPolicy) {
        return flushEvaluator.exhaustCardPatterns(cardGroup, cardPolicy).stream()
                .filter(flush -> straightEvaluator.isMatched(flush.getCards(), cardPolicy))
                .map(AbstractCardPattern::getCards)
                .map(cards -> new StraightFlushCardPattern(cardPolicy,
                        cards[0], cards[1], cards[2], cards[3], cards[4]))
                .collect(Collectors.toSet());
    }


    public static class StraightFlushCardPattern extends StraightCardPattern {
        public StraightFlushCardPattern(CardPolicy cardPolicy,
                                        Card c1, Card c2, Card c3, Card c4, Card c5) {
            super(cardPolicy, c1, c2, c3, c4, c5);
        }
    }

}
