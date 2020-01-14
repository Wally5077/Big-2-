package big2.game;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.cards.Deck;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternEvaluator;
import big2.game.patterns.CardPatternInvalidException;
import big2.game.policies.Big2Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Big2Game {
    private CardPattern lastPlayPattern;
    private Messenger messenger;
    private List<Big2GameClientContext> playerClients = new ArrayList<>(4);
    private HashMap<Big2GameClient, HandCards> handCardsMap = new HashMap<>();
    private CardPatternEvaluator cardPatternEvaluator;
    private Big2Policy big2Policy;
    private boolean gameOver = false;
    private int turn = -1;

    public Big2Game(Messenger messenger,
                    CardPatternEvaluator cardPatternEvaluator,
                    Big2Policy big2Policy) {
        this.messenger = messenger;
        this.cardPatternEvaluator = cardPatternEvaluator;
        this.big2Policy = big2Policy;
    }

    public void addClientPlayer(Big2GameClient client) {
        playerClients.add(new Big2GameClientContext(this, messenger, client));
    }

    public void start() {
        distributeHandCards(createShuffledDeck());
        startNextPlayerTurn();
    }

    private Deck createShuffledDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        return deck;
    }

    private void distributeHandCards(Deck deck) {
        Card[][] cards = deck.deal(playerClients.size());
        for (int i = 0; i < playerClients.size(); i++) {
            HandCards handcards = new HandCards(cardPatternEvaluator, cards[i]);
            Big2GameClientContext clientContext = playerClients.get(i);
            handCardsMap.put(clientContext.getClient(), handcards);
            clientContext.getClient().onReceiveHandCards(handcards, clientContext);
        }
    }

    private void startNextPlayerTurn() {
        turn = turn + 1 >= playerClients.size() ? 0 : turn + 1;
        Big2GameClient client = playerClients.get(turn).getClient();
        client.onPlayerTurn(handCardsMap.get(client), big2Policy, playerClients.get(turn));
    }

    private boolean isGameOver() {
        return gameOver;
    }

    public void playCard(CardGroup cardPlay) {
        Big2GameClientContext context = getCurrentClientContext();

        try {
            evaluateAndCompleteCardPlay(cardPlay);
        } catch (CardPatternInvalidException err) {
            context.getClient().onCardPlayInvalid(cardPlay, context);
        }
    }

    private void evaluateAndCompleteCardPlay(CardGroup cardPlay) {
        CardPattern cardPlayPattern = cardPatternEvaluator.evaluate(cardPlay);
        Big2GameClientContext currentContext = getCurrentClientContext();
        if (big2Policy.isValidPlay(lastPlayPattern, cardPlayPattern)) {
            lastPlayPattern = cardPlayPattern;
            broadcast(ctx -> ctx.getClient().onNewCardPlay(getCurrentPlayer(), cardPlayPattern, ctx));
        } else {
            currentContext.getClient().onCardPlayInvalid(cardPlay, currentContext);
        }
    }

    public void pass() {
        Player currentPlayer = getCurrentPlayer();
        broadcast(ctx -> ctx.getClient().onPlayerPassed(currentPlayer, ctx));
        startNextPlayerTurn();
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }

    private void broadcast(Consumer<Big2GameClientContext> clientConsumer) {
        for (Big2GameClientContext clientContext : playerClients) {
            clientConsumer.accept(clientContext);
        }
    }

    private Player getCurrentPlayer() {
        return getCurrentClientContext().getPlayer();
    }

    private Big2GameClient getCurrentClient() {
        return getCurrentClientContext().getClient();
    }

    private Big2GameClientContext getCurrentClientContext() {
        return playerClients.get(turn);
    }

    public CardPattern getLastCardPlayPattern() {
        return lastPlayPattern;
    }
}
