package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;

public interface Big2Client {
	default HandCards getHandCards() {
		return getPlayer().getHandCards();
	}
	default int getId() {
		return getPlayer().getId();
	}

	Player getPlayer();
	void onGameStart(int yourId);
	void onPlayerTurn(Player player, boolean newRound, Big2ClientContext context);
	void onReceiveHandCards(HandCards handCards, Big2ClientContext context);
	void onNewCardPlay(Player player, CardPattern play, Big2ClientContext context);
	void onGameOver(Player winner, Messenger messenger);
	void onCardPlayInvalid(CardGroup play, Big2ClientContext context);
    void onPlayerPassed(Player player, Big2ClientContext ctx);
}
