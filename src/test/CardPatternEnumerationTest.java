package test;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Rank;
import big2.cards.Suit;
import big2.game.patterns.*;
import big2.game.patterns.FlushCardPatternEvaluatorAdapter.FlushCardPattern;
import big2.game.patterns.FullHouseCardPatternEvaluatorAdapter.FullHouseCardPattern;
import big2.game.patterns.PairCardPatternEvaluatorAdapter.PairCardPattern;
import big2.game.patterns.SingleCardPatternEvaluatorAdapter.SingleCardPattern;
import big2.game.patterns.StraightCardPatternEvaluatorAdapter.StraightCardPattern;
import big2.game.patterns.StraightFlushCardPatternEvaluatorAdapter.StraightFlushCardPattern;
import big2.game.policies.CardPolicy;
import big2.game.policies.StandardCardPolicy;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static big2.cards.Rank.*;
import static big2.cards.Suit.*;
import static org.junit.Assert.assertEquals;

;

public class CardPatternEnumerationTest {
    CardPolicy cardPolicy = new StandardCardPolicy();

    CardGroup cardGroup = new CardGroup(new Card(A, CLUB),
                                        new Card(A, HEART),
                                        new Card(R2, CLUB),
                                        new Card(R2, DIAMOND),
                                        new Card(R2, HEART),
                                        new Card(R2, Suit.SPADE),
                                        new Card(Rank.R3, CLUB),
                                        new Card(Rank.R3, DIAMOND),
                                        new Card(Rank.R5, HEART),
                                        new Card(Rank.R6, CLUB));

    @Test
    public void enumerateSingles() {
        SingleCardPatternEvaluatorAdapter adapter = new SingleCardPatternEvaluatorAdapter();
        CardGroup cardGroup = new CardGroup(new Card(A, CLUB),
                                    new Card(A, HEART),
                                    new Card(A, SPADE),
                                    new Card(R2, DIAMOND));

        List<Card[]> expected = Arrays.stream(cardGroup.getCards())
                                    .map(c -> new Card[]{c})
                                    .collect(Collectors.toList());

        Set<SingleCardPattern> cardPatterns = adapter.enumerateCardPatterns(cardGroup, cardPolicy);

        assertCardsCombinationEqualWithoutOrder(expected, cardPatternsToListOfCards(cardPatterns));
    }

    @Test
    public void enumeratePairs() {
        PairCardPatternEvaluatorAdapter adapter = new PairCardPatternEvaluatorAdapter();
        CardGroup cardGroup = new CardGroup(new Card(A, CLUB),
                new Card(A, HEART),
                new Card(A, SPADE),
                new Card(R2, DIAMOND));

        List<Card[]> expected = Arrays.asList(new Card[]{new Card(A, CLUB), new Card(A, HEART)},
                                            new Card[]{new Card(A, CLUB), new Card(A, SPADE)},
                                            new Card[]{new Card(A, HEART), new Card(A, SPADE)});

        Set<PairCardPattern> cardPatterns = adapter.enumerateCardPatterns(cardGroup, cardPolicy);

        assertCardsCombinationEqualWithoutOrder(expected, cardPatternsToListOfCards(cardPatterns));
    }

    @Test
    public void enumerateStraights() {
        StraightCardPatternEvaluatorAdapter adapter = new StraightCardPatternEvaluatorAdapter();
        CardGroup cardGroup = new CardGroup(false, new Card(A, CLUB),  //0
                                        new Card(R2, HEART), //1
                                        new Card(R2, DIAMOND), //2
                                        new Card(R2, SPADE), //3
                                        new Card(R9, CLUB), //4
                                        new Card(R10, DIAMOND), //5
                                        new Card(R10, SPADE), //6
                                        new Card(J, HEART), //7
                                        new Card(Q, SPADE), //8
                                        new Card(K, CLUB)); //9

        List<Card[]> expected = Arrays.asList(
                cardGroup.selectIndices(4, 5, 7, 8, 9).getCards(),
                cardGroup.selectIndices(4, 6, 7, 8, 9).getCards(),
                cardGroup.selectIndices(5, 7, 8, 9, 0).getCards(),
                cardGroup.selectIndices(6, 7, 8, 9, 0).getCards(),
                cardGroup.selectIndices(7, 8, 9, 0, 1).getCards(),
                cardGroup.selectIndices(7, 8, 9, 0, 2).getCards(),
                cardGroup.selectIndices(7, 8, 9, 0, 3).getCards());

        Set<StraightCardPattern> cardPatterns = adapter.enumerateCardPatterns(cardGroup, cardPolicy);

        assertCardsCombinationEqualWithoutOrder(expected, cardPatternsToListOfCards(cardPatterns));
    }
    @Test
    public void enumerateFullHouse() {
        FullHouseCardPatternEvaluatorAdapter adapter = new FullHouseCardPatternEvaluatorAdapter();
        CardGroup cardGroup = new CardGroup(false, new Card(R7, CLUB),  //0
                                        new Card(R2, HEART), //1
                                        new Card(R2, DIAMOND), //2
                                        new Card(R2, SPADE), //3
                                        new Card(R3, CLUB), //4
                                        new Card(R3, DIAMOND), //5
                                        new Card(R4, SPADE), //6
                                        new Card(R4, HEART), //7
                                        new Card(A, HEART), //8
                                        new Card(A, DIAMOND), //9
                                        new Card(A, SPADE)); //10

        List<Card[]> expected = Arrays.asList(
                cardGroup.selectIndices(1, 2, 3, 4, 5).getCards(),  // 22233
                cardGroup.selectIndices(1, 2, 3, 6, 7).getCards(),  // 22244
                cardGroup.selectIndices(1, 2, 3, 8, 9).getCards(),  // 222AA (1)
                cardGroup.selectIndices(1, 2, 3, 8, 10).getCards(),  // 222AA (2)
                cardGroup.selectIndices(1, 2, 3, 9, 10).getCards(),  // 222AA (3)
                cardGroup.selectIndices(8, 9, 10, 4, 5).getCards(),  // AAA33
                cardGroup.selectIndices(8, 9, 10, 6, 7).getCards(),  // AAA44
                cardGroup.selectIndices(8, 9, 10, 1, 2).getCards(),  // AAA22 (1)
                cardGroup.selectIndices(8, 9, 10, 1, 3).getCards(),  // AAA22 (2)
                cardGroup.selectIndices(8, 9, 10, 2, 3).getCards());  // AAA22 (3)

        Set<FullHouseCardPattern> cardPatterns = adapter.enumerateCardPatterns(cardGroup, cardPolicy);

        assertCardsCombinationEqualWithoutOrder(expected, cardPatternsToListOfCards(cardPatterns));
    }

    private List<Card[]> cardPatternsToListOfCards(Set<? extends CardPattern> cardPatterns) {
        return cardPatterns.stream()
                .map(CardPattern::getCards)
                .collect(Collectors.toList());
    }

    private void assertCardsCombinationEqualWithoutOrder(List<Card[]> expect, List<Card[]> actual) {
        assertEquals(listOfCardsToSet(expect), listOfCardsToSet(actual));
    }


    private Set<Set<Card>> listOfCardsToSet(List<Card[]> expect) {
        return expect.stream()
                .map(cards -> new HashSet<>(Arrays.asList(cards)))
                .collect(Collectors.toSet());
    }

}
