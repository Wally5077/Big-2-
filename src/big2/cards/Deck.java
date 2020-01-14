package big2.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards = new Stack<>();
    private Stack<Card> discards = new Stack<>();

    public Deck() {
        init();
    }

    private void init() {
        Suit[] suits = Suit.values();
        Rank[] ranks = Rank.values();

        for (Rank rank : ranks) {
            for (Suit suit : suits) {
                cards.push(new Card(rank, suit));
            }
        }
    }

    public Card deal() {
        if (cards.isEmpty())
            throw new DeckEmptyException();
        return cards.pop();
    }

    public void shuffle() {
        cards.addAll(discards);
        Collections.shuffle(cards);
    }

    public void discard(Card card) {
        discards.push(card);
    }

    public Card[][] deal(int numOfDivision) {
        int size = cards.size();
        int eachSize = size / numOfDivision;
        Card[][] division = new Card[numOfDivision][eachSize];

        for (int i = 0; i < numOfDivision; i++) {
            for (int j = 0; j < eachSize; j++) {
                division[i][j] = deal();
            }
        }

        //TODO this assumes the numOfDivision should be at most 4
        int remaining = size % numOfDivision;
        if (remaining != 0) {
            Card[] firstCards = division[0];
            division[0] = new Card[remaining + eachSize];
            System.arraycopy(firstCards, 0, division[0], 0, firstCards.length);
            for (int i = 1; i <= remaining; i++) {
                division[0][division[0].length-i] = deal();
            }
        }

        return division;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int lineCount = 1;
        for (Card card : cards) {
            stringBuilder.append(card);
            if (lineCount ++ % 13 == 0)
                stringBuilder.append("\n ");
            else
                stringBuilder.append(", ");

        }
        return stringBuilder.substring(0, stringBuilder.length()-2);  //trim the last comma
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println(deck);

        for (Card[] cards : deck.deal(3)) {
            System.out.println(Arrays.toString(cards));
        }
    }
}
