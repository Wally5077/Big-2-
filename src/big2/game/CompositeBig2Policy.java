package big2.game;

import big2.cards.Card;
import big2.cards.Rank;
import big2.game.patterns.CardPattern;
import big2.game.policies.Big2Policy;
import big2.game.policies.CardPatternPolicy;
import big2.game.policies.CardPlayPolicy;
import big2.game.policies.CardPolicy;

import java.util.Collection;

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
    public int getTotalLevel(CardPattern cardPattern) {
        return cardPatternPolicy.getTotalLevel(cardPattern);
    }

    @Override
    public int getCardPatternBaseLevel(Class<? extends CardPattern> type) {
        return cardPatternPolicy.getCardPatternBaseLevel(type);
    }

    @Override
    public Collection<Class<? extends CardPattern>> getCeilingLevelCardPatternTypes(Class<? extends CardPattern> type) {
        return cardPatternPolicy.getCeilingLevelCardPatternTypes(type);
    }

    @Override
    public Collection<Class<? extends CardPattern>> getFloorLevelCardPatternTypes(Class<? extends CardPattern> type) {
        return cardPatternPolicy.getFloorLevelCardPatternTypes(type);
    }

    @Override
    public boolean isValidPlay(CardPattern lastPlay, CardPattern currentPlay) {
        return cardPlayPolicy.isValidPlay(lastPlay, currentPlay);
    }
}
