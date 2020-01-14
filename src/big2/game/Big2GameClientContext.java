package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;

public final class Big2GameClientContext {
    private Big2GameClient client;
    private Big2Game big2Game;
    private Messenger messenger;

    public Big2GameClientContext(Big2Game big2Game,
                                 Messenger messenger,
                                 Big2GameClient client) {
        this.big2Game = big2Game;
        this.messenger = messenger;
        this.client = client;
    }

    public CardPattern getLastCardPlayPattern() {
        return big2Game.getLastCardPlayPattern();
    }

    public void talk(String msg) {
        messenger.talk(client.getPlayer().getName(), msg);
    }

    public void playCard(CardGroup cardGroup) {
        big2Game.playCard(cardGroup);
    }

    public void pass() {
        big2Game.pass();
    }

    public Player getPlayer() {
        return getClient().getPlayer();
    }

    public Big2GameClient getClient() {
        return client;
    }

    public void playCard(CardPattern cardPattern) {
        big2Game.playCard(cardPattern);
    }
}
