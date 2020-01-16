package big2.game.patterns;

import java.util.Arrays;
import java.util.List;

public class StandardCardPatternAdapters {
    public static List<CardPatternAdapter> get() {
        FlushCardPatternAdapter flushCardPatternEvaluatorAdapter = new FlushCardPatternAdapter();
        StraightCardPatternAdapter straightCardPatternEvaluatorAdapter = new StraightCardPatternAdapter();
        return Arrays.asList(
                new SingleCardPatternAdapter(),
                new PairCardPatternAdapter(),
                new FullHouseCardPatternAdapter(),
                straightCardPatternEvaluatorAdapter,
                /*flushCardPatternEvaluatorAdapter,*/
                new FourOfRankCardPatternEvaluatingAdapter(),
                new StraightFlushCardPatternAdapter(
                        flushCardPatternEvaluatorAdapter,
                        straightCardPatternEvaluatorAdapter)
        );
    }
}
