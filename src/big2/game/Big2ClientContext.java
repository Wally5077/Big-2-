package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.policies.Big2Policy;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface Big2ClientContext {
    boolean isNewRound();

    CardPattern getLastCardPlayPattern();

    void talk(String msg);

    void playCard(CardGroup cardGroup);

    void playCard(CardPattern cardPattern);

    void pass();

    Big2Policy getBig2Policy();
}
