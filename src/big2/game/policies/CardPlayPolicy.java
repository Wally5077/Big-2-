package big2.game.policies;

import big2.game.patterns.CardPattern;

public interface CardPlayPolicy {

	boolean isValidPlay(CardPattern lastPlay, CardPattern currentPlay);

}
