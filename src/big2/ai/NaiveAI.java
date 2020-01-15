package big2.ai;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.Big2GameClientContext;
import big2.game.HandCards;
import big2.game.Messenger;
import big2.game.Player;
import big2.game.patterns.CardPatternEnumeration;
import big2.game.policies.Big2Policy;

import java.util.List;

public class NaiveAI extends AI {
    private Player player;

    public NaiveAI(String name) {
        this.player = new Player(name);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void onPlayerTurn(HandCards handCards, Big2Policy policy, Big2GameClientContext context) {
        CardPattern ceilingPattern = handCards.enumerateCardPatterns()
                .ceilingCardPattern(context.getLastCardPlayPattern());

        if (ceilingPattern == null)
            context.pass();
        else
            context.playCard(ceilingPattern);
    }

    @Override
    public void onReceiveHandCards(HandCards handCards, Big2GameClientContext context) {

    }

    @Override
    public void onNewCardPlay(Player player, CardPattern play, Big2GameClientContext context) {

    }

    @Override
    public void onGameOver(Player winner, Messenger messenger) {

    }

    @Override
    public void onCardPlayInvalid(CardGroup play, Big2GameClientContext context) {

    }

    @Override
    public void onPlayerPassed(Player player, Big2GameClientContext ctx) {

    }

}
