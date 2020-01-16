package big2.game;

import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;

public interface Big2Client {
	String getName();
	void onGameStart(Player you);
	void onPlayerTurn(boolean isYourTurn, Player player, boolean newRound, Big2ClientContext context);
	void onNewCardPlay(Player player, CardPattern play, Big2ClientContext context);
	void onGameOver(Player winner, Messenger messenger);
	void onCardPlayInvalid(CardGroup play, Big2ClientContext context);
    void onPlayerPassed(Player player, Big2ClientContext ctx);
}
