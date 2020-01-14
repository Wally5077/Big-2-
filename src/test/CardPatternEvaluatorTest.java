package test;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.cards.Suit;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternEvaluatorAdapter;
import big2.game.patterns.SingleCardPatternAbstractFactory;
import big2.game.policies.CardPolicy;
import big2.game.policies.StandardCardPolicy;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CardPatternEvaluatorTest {
    private CardPolicy cardPolicy = new StandardCardPolicy();

    @Test
    public void testSingleCardPatternEvaluatorAdapter() {
        SingleCardPatternAbstractFactory factory = new SingleCardPatternAbstractFactory();
        CardPatternEvaluatorAdapter adapter = factory.createEvaluatorAdapter();

        Card card = new Card(Rank.A, Suit.CLUB);
        CardGroup cardGroup = new CardGroup(card);

        assertTrue(adapter.isMatched(cardGroup, ));

        CardPattern cardPattern = adapter.create(cardGroup, cardPolicy);
        assertTrue(cardPattern instanceof SingleCardPattern);

        SingleCardPattern singleCardPattern = (SingleCardPattern) cardPattern;
        assertEquals(card, singleCardPattern.getCard());
    }
}