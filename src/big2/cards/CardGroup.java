package big2.cards;

import big2.game.policies.CardPolicy;
import big2.utils.ArrayUtils;
import big2.utils.CardUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CardGroup implements Iterable<Card> {
    private boolean sorted;
    private Card[] cards;

    CardGroup(boolean sort, List<Card> cardList) {
        this.cards = new Card[cardList.size()];
        cardList.toArray(cards);
        if (sort) {
            Arrays.sort(cards, this::compareCards);
            this.sorted = true;
        }
    }

    public CardGroup(List<Card> cardList) {
        this(true, cardList);
    }

    public CardGroup(Card ...cards) {
        this(true, Arrays.asList(cards));
    }

    public CardGroup(boolean sort, Card ...cards) {
        this(sort, Arrays.asList(cards));
    }

    public CardGroup selectRange(int startInclusive, int endExclusive) {
        sortIfNotSorted();
        Card[] sub = new Card[endExclusive - startInclusive];
        System.arraycopy(cards, startInclusive, sub, 0, sub.length);
        return new CardGroup(false, sub);
    }

    public CardGroup selectByCyclicLength(int startInclusive, int cyclicLength) {
        return new CardGroup(false,
                IntStream.range(0, cyclicLength)
                    .map(i -> (startInclusive+i) % cards.length)
                    .mapToObj(i -> cards[i])
                    .toArray(Card[]::new));

    }

    public CardGroup selectIndices(int ...indices) {
        sortIfNotSorted();
        return new CardGroup(false, getCards(indices));
    }

    public CardGroup remove(Card[] removedCards) {
        return new CardGroup(false, Arrays.stream(this.cards)
                .filter(c -> !ArrayUtils.contains(removedCards, c))
                .toArray(Card[]::new));
    }

    public Card get(int index) {
        sortIfNotSorted();
        return cards[index];
    }

    public Stream<Card> stream() {
        sortIfNotSorted();
        return Arrays.stream(cards);
    }

    public Card[] getCards(int ...indices) {
        sortIfNotSorted();
        Card[] cards = new Card[indices.length];
        int idx = 0;
        for (int index : indices) {
            cards[idx++] = this.cards[index];
        }
        return cards;
    }

    public Card[] getCards() {
        sortIfNotSorted();
        return cards;
    }

    @Override
    public Iterator<Card> iterator() {
        sortIfNotSorted();
        return new Iterator<Card>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < cards.length;
            }

            @Override
            public Card next() {
                return cards[index ++];
            }
        };
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return cards.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardGroup cards1 = (CardGroup) o;
        return Arrays.equals(cards, cards1.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }

    private void sortIfNotSorted() {
        if (!sorted) {
            Arrays.sort(cards, this::compareCards);
            sorted = true;
        }
    }

    protected int compareCards(Card c1, Card c2) {
        return c1.getRank().ordinal() - c2.getRank().ordinal();
    }

    public boolean allInSameRank() {
        return CardUtils.sameRanks(cards);
    }

    public boolean allInSameSuit() {
        return CardUtils.sameSuits(cards);
    }

    public boolean isContinuousRank(CardPolicy cardPolicy) {
        sortIfNotSorted();

        Rank assertNextRank = get(0).getRank();
        for (Card card : this) {
            if (assertNextRank != card.getRank()) {
                return false;
            }
            assertNextRank = cardPolicy.getNextRank(card.getRank());
        }
        return true;
    }

    public List<CardGroup> divideByRank() {
        return Arrays.stream(cards)
                .collect(Collectors.groupingBy(Card::getRank))
                .values().stream()
                .map(cards -> new CardGroup(false, cards))
                .collect(Collectors.toList());
    }

    public boolean contains(Card card) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].equals(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        sortIfNotSorted();
        return Arrays.toString(cards);
    }
}
