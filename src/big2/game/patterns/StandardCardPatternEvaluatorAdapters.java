package big2.game.patterns;

import java.util.Arrays;
import java.util.List;

public class StandardCardPatternEvaluatorAdapters {
    public static List<CardPatternEvaluatorAdapter> get() {
        FlushCardPatternEvaluatorAdapter flushCardPatternEvaluatorAdapter = new FlushCardPatternEvaluatorAdapter();
        StraightCardPatternEvaluatorAdapter straightCardPatternEvaluatorAdapter = new StraightCardPatternEvaluatorAdapter();
        return Arrays.asList(
                new SingleCardPatternEvaluatorAdapter(),
                new PairCardPatternEvaluatorAdapter(),
                new FullHouseCardPatternEvaluatorAdapter(),
                straightCardPatternEvaluatorAdapter,
                flushCardPatternEvaluatorAdapter,
                new FourOfRankCardPatternEvaluatorAdapter(),
                new StraightFlushCardPatternEvaluatorAdapter(
                        flushCardPatternEvaluatorAdapter,
                        straightCardPatternEvaluatorAdapter)
        );
    }
}
