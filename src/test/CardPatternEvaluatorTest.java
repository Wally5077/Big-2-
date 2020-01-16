package test;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.cards.Suit;
import big2.game.patterns.*;
import big2.game.patterns.FlushCardPatternAdapter.FlushCardPattern;
import big2.game.patterns.FourOfRankCardPatternEvaluatingAdapter.FourOfRankCardPattern;
import big2.game.patterns.FullHouseCardPatternAdapter.FullHouseCardPattern;
import big2.game.patterns.PairCardPatternAdapter.PairCardPattern;
import big2.game.patterns.SingleCardPatternAdapter.SingleCardPattern;
import big2.game.patterns.StraightCardPatternAdapter.StraightCardPattern;
import big2.game.policies.CardPolicy;
import big2.game.policies.StandardCardPolicy;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardPatternEvaluatorTest {
    private CardPolicy cardPolicy = new StandardCardPolicy();

    @Test
    public void testSingleCardPatternEvaluatorAdapter() {
        SingleCardPatternAdapter adapter = new SingleCardPatternAdapter();

        Card card = new Card(Rank.A, Suit.CLUB);
        CardGroup cardGroup = new CardGroup(card);

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        SingleCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertEquals(card, cardPattern.getCard());
    }

    @Test
    public void testPairCardPatternEvaluatorAdapter() {
        PairCardPatternAdapter adapter = new PairCardPatternAdapter();

        CardGroup cardGroup = new CardGroup(new Card(Rank.R2, Suit.CLUB), new Card(Rank.R2, Suit.DIAMOND));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        PairCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertArrayEquals(cardGroup.getCards(), cardPattern.getCards());
    }

    @Test
    public void testStraightEvaluatorAdapter() {
        StraightCardPatternAdapter adapter = new StraightCardPatternAdapter();

        CardGroup cardGroup = new CardGroup(
                new Card(Rank.A, Suit.CLUB), new Card(Rank.R2, Suit.DIAMOND),
                new Card(Rank.R3, Suit.DIAMOND), new Card(Rank.R4, Suit.DIAMOND),
                new Card(Rank.R5, Suit.SPADE));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        StraightCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertArrayEquals(cardGroup.getCards(), cardPattern.getCards());
    }

    @Test
    public void testStraightFlushEvaluatorAdapter() {
        StraightFlushCardPatternAdapter adapter =
                new StraightFlushCardPatternAdapter(new FlushCardPatternAdapter(), new StraightCardPatternAdapter());

        CardGroup cardGroup = new CardGroup(
                new Card(Rank.A, Suit.CLUB), new Card(Rank.R2, Suit.CLUB),
                new Card(Rank.R3, Suit.CLUB), new Card(Rank.R4, Suit.CLUB),
                new Card(Rank.R5, Suit.CLUB));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        StraightCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertArrayEquals(cardGroup.getCards(), cardPattern.getCards());
    }

    @Test
    public void testFlushEvaluatorAdapter() {
        FlushCardPatternAdapter adapter = new FlushCardPatternAdapter();

        CardGroup cardGroup = new CardGroup(
                new Card(Rank.A, Suit.CLUB), new Card(Rank.J, Suit.CLUB),
                new Card(Rank.R4, Suit.CLUB), new Card(Rank.K, Suit.CLUB),
                new Card(Rank.R8, Suit.CLUB));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        FlushCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertArrayEquals(cardGroup.getCards(), cardPattern.getCards());
    }

    @Test
    public void testFullHouseEvaluatorAdapter() {
        FullHouseCardPatternAdapter adapter = new FullHouseCardPatternAdapter();

        CardGroup cardGroup = new CardGroup(
                new Card(Rank.R4, Suit.CLUB), new Card(Rank.R4, Suit.DIAMOND),
                new Card(Rank.R4, Suit.SPADE), new Card(Rank.J, Suit.DIAMOND),
                new Card(Rank.J, Suit.SPADE));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        FullHouseCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertEquals(Rank.R4, cardPattern.getThreeCardsPart()[0].getRank());
        assertEquals(Rank.J, cardPattern.getTwoCardsPart()[0].getRank());
    }

    @Test
    public void testFourOfRankEvaluatorAdapter() {
        FourOfRankCardPatternEvaluatingAdapter adapter = new FourOfRankCardPatternEvaluatingAdapter();

        CardGroup cardGroup = new CardGroup(
                new Card(Rank.R4, Suit.CLUB), new Card(Rank.R4, Suit.DIAMOND),
                new Card(Rank.R4, Suit.SPADE), new Card(Rank.R4, Suit.HEART),
                new Card(Rank.R2, Suit.SPADE));

        assertTrue(adapter.isMatched(cardGroup, cardPolicy));

        FourOfRankCardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertEquals(Rank.R4, cardPattern.getFourCardsPart()[0].getRank());
        assertEquals(Rank.R2, cardPattern.getFoot().getRank());
    }
}