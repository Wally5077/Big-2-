package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;

public interface Big2GameClient {
	Player getPlayer();
	void onGameStart(int yourId);
	void onPlayerTurn(Player player, boolean newRound, Big2GameClientContext context);
	void onReceiveHandCards(HandCards handCards, Big2GameClientContext context);
	void onNewCardPlay(Player player, CardPattern play, Big2GameClientContext context);
	void onGameOver(Player winner, Messenger messenger);
	void onCardPlayInvalid(CardGroup play, Big2GameClientContext context);
    void onPlayerPassed(Player player, Big2GameClientContext ctx);
}
