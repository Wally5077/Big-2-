package big2.game.policies;

import big2.game.patterns.FlushCardPatternAdapter.FlushCardPattern;
import big2.game.patterns.FourOfRankCardPatternEvaluatingAdapter.FourOfRankCardPattern;
import big2.game.patterns.FullHouseCardPatternAdapter.FullHouseCardPattern;
import big2.game.patterns.PairCardPatternAdapter.PairCardPattern;
import big2.game.patterns.SingleCardPatternAdapter.SingleCardPattern;
import big2.game.patterns.StraightCardPatternAdapter.StraightCardPattern;
import big2.game.patterns.StraightFlushCardPatternAdapter.StraightFlushCardPattern;

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
