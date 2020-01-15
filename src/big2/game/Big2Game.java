package big2.game;

import big2.cards.*;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternEvaluator;
import big2.game.patterns.CardPatternInvalidException;
import big2.game.policies.Big2Policy;
import big2.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Big2Game {
    private final static Card CLUB3 = new Card(Rank.R3, Suit.CLUB);
    private CardPattern lastPlayPattern;
    private Messenger messenger;
    private List<Big2GameClientContext> playerClients = new ArrayList<>(4);
    private HashMap<Big2GameClient, HandCards> handCardsMap = new HashMap<>();
    private CardPatternEvaluator cardPatternEvaluator;
    private Big2Policy big2Policy;
    private int pass = 0;
    private int turn = -1;

    public Big2Game(Messenger messenger,
                    CardPatternEvaluator cardPatternEvaluator,
                    Big2Policy big2Policy) {
        this.messenger = messenger;
        this.cardPatternEvaluator = cardPatternEvaluator;
        this.big2Policy = big2Policy;
    }

    public void addClientPlayer(Big2GameClient client) {
        int id = playerClients.size();
        playerClients.add(new Big2GameClientContext(id, this, messenger, client));
    }

    public void start() {
        distributeHandCards(createShuffledDeck());
        broadcast(ctx -> ctx.getClient().onGameStart(ctx.getId()));
        startTheClub3PlayerTurn();
        startNextPlayerTurn(false);
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
            // find the first player who holds club 3
            if (turn == -1 && ArrayUtils.contains(cards[i], CLUB3)) {
                turn = i;
            }
            Big2GameClientContext clientContext = playerClients.get(i);
            handCardsMap.put(clientContext.getClient(), handcards);
        }
    }

    private void startTheClub3PlayerTurn() {
        playCard(new CardGroup(CLUB3));
    }

    public void playCard(CardGroup cardPlay) {
        Big2GameClientContext context = getCurrentClientContext();
        Big2GameClient client = context.getClient();

        try {
            if (handCardsMap.get(getCurrentClient()).contains(cardPlay)) {
                playCard(cardPatternEvaluator.evaluate(cardPlay));
            } else {
                client.onCardPlayInvalid(cardPlay, context);
            }

        } catch (CardPatternInvalidException err) {
            client.onCardPlayInvalid(cardPlay, context);
        }
    }

    public void playCard(CardPattern cardPlayPattern) {
        overrideLastPlayPatterIfValid(cardPlayPattern);
        startNextPlayerTurnOrGameOver();
    }

    private void overrideLastPlayPatterIfValid(CardPattern cardPlayPattern) {
        if (lastPlayPattern == null || big2Policy.isValidPlay(lastPlayPattern, cardPlayPattern)) {
            lastPlayPattern = cardPlayPattern;
            HandCards currentHandCards = handCardsMap.get(getCurrentClient());
            handCardsMap.put(getCurrentClient(), currentHandCards.exclude(cardPlayPattern.getCards()));
            broadcast(ctx -> ctx.getClient().onNewCardPlay(getCurrentPlayer(), cardPlayPattern, ctx));
        } else {
            Big2GameClientContext context = getCurrentClientContext();
            context.getClient().onCardPlayInvalid(new CardGroup(cardPlayPattern.getCards()), context);
        }
    }

    private void startNextPlayerTurnOrGameOver() {
        if (!isGameOver()) {
            startNextPlayerTurn(false);
        } else {
            broadcast(ctx -> ctx.getClient().onGameOver(getCurrentPlayer(), messenger));
        }
    }

    private boolean isGameOver() {
        return handCardsMap.get(getCurrentClient()).isEmpty();
    }

    public void pass() {
        Player currentPlayer = getCurrentPlayer();
        boolean newRound = false;
        if (pass ++ == playerClients.size()) {
            pass = 0;
            newRound = true;
            lastPlayPattern = null;
        }
        broadcast(ctx -> ctx.getClient().onPlayerPassed(currentPlayer, ctx));
        startNextPlayerTurn(newRound);
    }

    private void startNextPlayerTurn(boolean newRound) {
        turn = turn + 1 >= playerClients.size() ? 0 : turn + 1;
        Big2GameClientContext context = getCurrentClientContext();
        context.getClient().onReceiveHandCards(handCardsMap.get(context.getClient()), context);
        broadcast(ctx -> ctx.getClient().onPlayerTurn(context.getPlayer(), newRound, playerClients.get(turn)));
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

    public Big2Policy getPolicy() {
        return big2Policy;
    }
}
