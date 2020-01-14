package big2.game.patterns;

import big2.cards.Card;
import big2.game.policies.CardPolicy;

import java.util.Arrays;

public abstract class AbstractCardPattern implements CardPattern {
    protected CardPolicy cardPolicy;
    protected Card[] cards;

    public AbstractCardPattern(CardPolicy cardPolicy, Card ...cards) {
        this.cards = cards;
        this.cardPolicy = cardPolicy;
    }

    public AbstractCardPattern(CardPolicy cardPolicy, Card[] ...cardParts) {
        this(cardPolicy, Arrays.stream(cardParts)
                .flatMap(Arrays::stream)
                .toArray(Card[]::new));
    }


    @Override
    public Card[] getCards() {
        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCardPattern that = (AbstractCardPattern) o;
        return Arrays.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }

    @Override
    public int compareTo(CardPattern o) {
        return getLevel() - o.getLevel();
    }
}
