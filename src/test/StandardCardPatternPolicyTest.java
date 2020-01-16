package test;

import big2.game.patterns.FlushCardPatternAdapter.FlushCardPattern;
import big2.game.patterns.FourOfRankCardPatternEvaluatingAdapter.FourOfRankCardPattern;
import big2.game.patterns.FullHouseCardPatternAdapter.FullHouseCardPattern;
import big2.game.patterns.SingleCardPatternAdapter.SingleCardPattern;
import big2.game.patterns.PairCardPatternAdapter.PairCardPattern;
import big2.game.patterns.StraightCardPatternAdapter.StraightCardPattern;
import big2.game.patterns.StraightFlushCardPatternAdapter.StraightFlushCardPattern;
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