package big2.game;

import big2.cards.Card;
import big2.cards.Rank;
import big2.game.patterns.CardPattern;
import big2.game.policies.Big2Policy;
import big2.game.policies.CardPatternPolicy;
import big2.game.policies.CardPlayPolicy;
import big2.game.policies.CardPolicy;

import java.util.NavigableSet;

public class CompositeBig2Policy implements Big2Policy {
    private CardPolicy cardPolicy;
    private CardPatternPolicy cardPatternPolicy;
    private CardPlayPolicy cardPlayPolicy;

    public CompositeBig2Policy(CardPolicy cardPolicy,
                               CardPatternPolicy cardPatternPolicy,
                               CardPlayPolicy cardPlayPolicy) {
        this.cardPolicy = cardPolicy;
        this.cardPatternPolicy = cardPatternPolicy;
        this.cardPlayPolicy = cardPlayPolicy;
    }

    @Override
    public int getLevel(Card card) {
        return cardPolicy.getLevel(card);
    }

    @Override
    public Rank getNextRank(Rank rank) {
        return cardPolicy.getNextRank(rank);
    }


    @Override
    public boolean isValidPlay(CardPattern lastPlay, CardPattern currentPlay) {
        return cardPlayPolicy.isValidPlay(lastPlay, currentPlay);
    }

    @Override
    public int compare(CardPattern cardPattern1, CardPattern cardPattern2) {
        return cardPatternPolicy.compare(cardPattern1, cardPattern2);
    }

    @Override
    public int compare(Class<? extends CardPattern> type1, Class<? extends CardPattern> type2) {
        return cardPatternPolicy.compare(type1, type2);
    }

    @Override
    public NavigableSet<Class<? extends CardPattern>> getOrderedCardPatternTypes() {
        return cardPatternPolicy.getOrderedCardPatternTypes();
    }
}
