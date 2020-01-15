package test;

import big2.game.patterns.FlushCardPatternEvaluatorAdapter.FlushCardPattern;
import big2.game.patterns.FourOfRankCardPatternEvaluatorAdapter.FourOfRankCardPattern;
import big2.game.patterns.FullHouseCardPatternEvaluatorAdapter.FullHouseCardPattern;
import big2.game.patterns.SingleCardPatternEvaluatorAdapter.SingleCardPattern;
import big2.game.patterns.PairCardPatternEvaluatorAdapter.PairCardPattern;
import big2.game.patterns.StraightCardPatternEvaluatorAdapter.StraightCardPattern;
import big2.game.patterns.StraightFlushCardPatternEvaluatorAdapter.StraightFlushCardPattern;
import big2.game.policies.StandardCardPatternPolicy;
import big2.game.policies.StandardCardPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardCardPatternPolicyTest {

    @Test
    public void test() {
        StandardCardPatternPolicy standardCardPatternPolicy = new StandardCardPatternPolicy(new StandardCardPolicy());
        assertEquals(-1, standardCardPatternPolicy.compare(SingleCardPattern.class, PairCardPattern.class));
        assertEquals(-1, standardCardPatternPolicy.compare(PairCardPattern.class, FlushCardPattern.class));
        assertEquals(-1, standardCardPatternPolicy.compare(FlushCardPattern.class, StraightCardPattern.class));
        assertEquals(-1, standardCardPatternPolicy.compare(StraightCardPattern.class, FullHouseCardPattern.class));
        assertEquals(-1, standardCardPatternPolicy.compare(FullHouseCardPattern.class, FourOfRankCardPattern.class));
        assertEquals(-1, standardCardPatternPolicy.compare(FourOfRankCardPattern.class, StraightFlushCardPattern.class));
    }
}