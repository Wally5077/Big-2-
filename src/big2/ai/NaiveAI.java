package big2.ai;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.HandCards;
import big2.game.Messenger;
import big2.game.Player;
import big2.game.patterns.CardPatternExhaustion;

public class NaiveAI extends AI {
    private Player player;
    private HandCards handCards;

    public NaiveAI(String name) {
        this.player = new Player(name);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void onGameStart(int yourId) {
        player.setName(player.getName() + "-" + yourId);
    }

    @Override
    public void onReceiveHandCards(HandCards handCards, Big2GameClientContext context) {
        this.handCards = handCards;
    }

    @Override
    public void onPlayerTurn(Player player, boolean newRound, Big2GameClientContext context) {
        if (player.getId() == this.player.getId()) {
            CardPattern lastPlay = context.getLastCardPlayPattern();
            CardPatternExhaustion exhaustion = handCards.exhaustCardPatterns();
            if (lastPlay == null) {
                context.playCard(exhaustion.pollLastCardPattern());
            } else {
                CardPattern ceilingPattern = exhaustion.ceilingCardPattern(lastPlay);

                if (ceilingPattern == null || ceilingPattern.getClass() != lastPlay.getClass())
                    context.pass();
                else
                    context.playCard(ceilingPattern);
            }
        }
    }

    @Override
    public void onNewCardPlay(Player player, CardPattern play, Big2GameClientContext context) {
    }

    @Override
    public void onGameOver(Player winner, Messenger messenger) {
    }

    @Override
    public void onCardPlayInvalid(CardGroup play, Big2GameClientContext context) {
        throw new IllegalStateException("The AI's implementation is incorrect.");
    }

    @Override
    public void onPlayerPassed(Player player, Big2GameClientContext ctx) {
    }

}
