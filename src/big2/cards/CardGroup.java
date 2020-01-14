package big2.cards;

import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardGroup implements Iterable<Card> {
    private Card[] cards;

    public CardGroup(List<Card> cardList) {
        this.cards = new Card[cardList.size()];
        cardList.toArray(cards);
        Arrays.sort(cards, Comparator.comparingInt(o -> o.getRank().ordinal()));
    }

    public CardGroup(Card ...cards) {
        this.cards = cards;
        Arrays.sort(cards, Comparator.comparingInt(o -> o.getRank().ordinal()));
    }

    public CardGroup select(int ...indices) {
        return new CardGroup(getCards(indices));
    }

    public Card get(int index) {
        return cards[index];
    }

    public Stream<Card> stream() {
        return Arrays.stream(cards);
    }

    public Card[] getCards(int ...indices) {
        Card[] cards = new Card[indices.length];
        int idx = 0;
        for (int index : indices) {
            cards[idx++] = this.cards[index];
        }
        return cards;
    }

    public Card[] getCards() {
        return cards;
    }

    @Override
    public Iterator<Card> iterator() {
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

    public boolean allInSameRank() {
        return CardUtils.sameRanks(cards);
    }

    public boolean allInSameSuit() {
        return CardUtils.sameSuits(cards);
    }

    public boolean isContinuousRank(CardPolicy cardPolicy) {
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
                .map(CardGroup::new)
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
}
