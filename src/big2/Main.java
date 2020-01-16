package big2;

import big2.ai.NaiveAI;
import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.patterns.CardPattern;
import big2.game.*;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Big2Game big2Game = new Big2GameBuilder().build();
        big2Game.addClientPlayer(new Client("Johnny"));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.addClientPlayer(new NaiveAI("AI"));
        big2Game.start();
    }

    public static class Client implements Big2Client {
        private Scanner scanner = new Scanner(System.in);
        private String name;
        private HandCards handCards;

        public Client(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void onGameStart(Player you) {
            System.out.println("The game is started!");
        }

        @Override
        public void onPlayerTurn(boolean isYourTurn, Player player, boolean newRound, Big2ClientContext context) {
            System.out.printf("It's %s's turn!\n", player.getName());

            if (isYourTurn) {
                this.handCards = player.getHandCards();
                printHandCardsInfo();
                doMyTurn(context);
            }
        }

        private void doMyTurn(Big2ClientContext context) {
            int[] indices = askInputForCardIndicesWithFoolProof();
            if (indices[0] == -1) {
                context.pass();
            } else {
                context.playCard(handCards.selectIndices(indices));
            }
        }

        private void printHandCardsInfo() {
            System.out.print("  ");
            for (int i = 0; i < handCards.size(); i++) {
                System.out.print(String.format("%3d  ", i));
            }
            System.out.println();

            System.out.print("[ ");
            for (int i = 0; i < handCards.size(); i++) {
                Card card = handCards.get(i);
                System.out.print(String.format("%3s", card.toString()));
                if (i != handCards.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(" ]\n -- Please select the card indices or type '-1' to pass:");
        }

        private int[] askInputForCardIndicesWithFoolProof() {
            try {
                int[] indices = askInputForCardIndices();
                return indices.length == 0 ? askInputForCardIndicesWithFoolProof() : indices;
            } catch (NumberFormatException err) {
                return askInputForCardIndicesWithFoolProof();
            }
        }

        private int[] askInputForCardIndices() {
            String line = scanner.nextLine();
            return Arrays.stream(line.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        @Override
        public void onNewCardPlay(Player player, CardPattern play, Big2ClientContext context) {
            System.out.printf("The player %s  plays: %s.\n", player.getName(), play);
        }

        @Override
        public void onGameOver(Player winner, Messenger messenger) {
            System.out.printf("The game is over, the winner is %s.\n", winner.getName());
        }

        @Override
        public void onCardPlayInvalid(CardGroup play, Big2ClientContext context) {
            System.out.println("The card play " + play + " is invalid.");
            printHandCardsInfo();
            doMyTurn(context);
        }

        @Override
        public void onPlayerPassed(Player player, Big2ClientContext ctx) {
            System.out.println("The player " + player.getName() + " has passed.");
        }
    }
}
