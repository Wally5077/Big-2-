package big2.game.policies;

import big2.game.patterns.CardPattern;

public class SamePatternPlayPolicy implements CardPlayPolicy {
    private CardPatternPolicy cardPatternPolicy;

    public SamePatternPlayPolicy(CardPatternPolicy cardPatternPolicy) {
        this.cardPatternPolicy = cardPatternPolicy;
    }

    @Override
    public boolean isValidPlay(CardPattern lastPlay, CardPattern currentPlay) {
        return lastPlay.getClass() == currentPlay.getClass()
                && cardPatternPolicy.compare(lastPlay, currentPlay) > 0;
    }
}
