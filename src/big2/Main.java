package big2;

import big2.ai.NaiveAI;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.*;
import big2.game.policies.Big2Policy;

public class Main {
    public static void main(String[] args) {
        Big2Game big2Game = new Big2GameBuilder().build();
        big2Game.addClientPlayer(new Client(new Player("Johnny")));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.start();
    }

    public static class Client implements Big2GameClient {
        private Player player;

        public Client(Player player) {
            this.player = player;
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public void onPlayerTurn(HandCards handCards,
                                 Big2Policy policy,
                                 Big2GameClientContext context) {

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
}
