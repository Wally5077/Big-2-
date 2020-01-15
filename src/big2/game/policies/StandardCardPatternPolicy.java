package big2.game.policies;

import big2.game.patterns.FlushCardPatternEvaluatorAdapter.FlushCardPattern;
import big2.game.patterns.FourOfRankCardPatternEvaluatorAdapter.FourOfRankCardPattern;
import big2.game.patterns.FullHouseCardPatternEvaluatorAdapter.FullHouseCardPattern;
import big2.game.patterns.PairCardPatternEvaluatorAdapter.PairCardPattern;
import big2.game.patterns.SingleCardPatternEvaluatorAdapter.SingleCardPattern;
import big2.game.patterns.StraightCardPatternEvaluatorAdapter.StraightCardPattern;
import big2.game.patterns.StraightFlushCardPatternEvaluatorAdapter.StraightFlushCardPattern;

public class StandardCardPatternPolicy extends OrderedCardPatternPolicy {

    public StandardCardPatternPolicy(CardPolicy cardPolicy) {
        super(cardPolicy);
        addLast(SingleCardPattern.class);
        addLast(PairCardPattern.class);
        addLast(FlushCardPattern.class);
        addLast(StraightCardPattern.class);
        addLast(FullHouseCardPattern.class);
        addLast(FourOfRankCardPattern.class);
        addLast(StraightFlushCardPattern.class);
    }

}
