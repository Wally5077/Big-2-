package big2.ai;

import big2.cards.CardGroup;
import big2.game.Big2ClientContext;
import big2.game.Messenger;
import big2.game.Player;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternExhaustedProduct;

import java.util.List;

public class NaiveAI extends AI {
    private String name;

    public NaiveAI(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onGameStart(Player you) {
        you.setName(you.getName() + "-" + you.getId());
    }

    @Override
    protected void onPlayerTurn(boolean isYourTurn, Player player, boolean newRound, List<CardPattern> validOptions, Big2ClientContext context) {
        if (isYourTurn) {
            CardPatternExhaustedProduct exhaustion = player.getHandCards().exhaustCardPatterns();
            if (context.isNewRound()) {
                context.playCard(exhaustion.pollLastCardPattern());
            } else {
                CardPattern lastPlay = context.getLastCardPlayPattern();
                exhaustion.higherCardPattern(lastPlay)
                        .ifPresent(context::playCard)
                        .orElse(context::pass);
            }
        }
    }

    @Override
    public void onNewCardPlay(Player player, CardPattern play, Big2ClientContext context) { }

    @Override
    public void onGameOver(Player winner, Messenger messenger) { }

    @Override
    public void onCardPlayInvalid(CardGroup play, Big2ClientContext context) {
        context.pass();
    }

    @Override
    public void onPlayerPassed(Player player, Big2ClientContext context) { }

}
