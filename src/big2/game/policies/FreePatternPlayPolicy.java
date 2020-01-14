package big2.game.policies;

import big2.game.patterns.CardPattern;

public class FreePatternPlayPolicy implements CardPlayPolicy {
    private CardPatternPolicy cardPatternPolicy;

    public FreePatternPlayPolicy(CardPatternPolicy cardPatternPolicy) {
        this.cardPatternPolicy = cardPatternPolicy;
    }

    @Override
    public boolean isValidPlay(CardPattern lastPlay, CardPattern currentPlay) {
        return cardPatternPolicy.compare(lastPlay, currentPlay) > 0;
    }
}
