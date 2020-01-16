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

public class Big2Game implements Big2ClientContext {
    private final static Card CLUB3 = new Card(Rank.R3, Suit.CLUB);
    private Deck deck;
    private CardPattern lastPlayPattern;
    private boolean newRound;
    private Messenger messenger;
    private List<Big2Client> clients = new ArrayList<>(4);
    private HashMap<Big2Client, HandCards> handCardsMap = new HashMap<>();
    private CardPatternEvaluator cardPatternEvaluator;
    private Big2Policy big2Policy;
    private int pass = 0;
    private int turn = -1;

    public Big2Game(Messenger messenger,
                    CardPatternEvaluator cardPatternEvaluator,
                    Big2Policy big2Policy, Deck deck) {
        this.deck = deck;
        this.messenger = messenger;
        this.cardPatternEvaluator = cardPatternEvaluator;
        this.big2Policy = big2Policy;
    }

    public void addClientPlayer(Big2Client client) {
        int id = clients.size();
        client.getPlayer().setId(id);
        clients.add(client);
    }

    public void start() {
        distributeHandCards();
        broadcast(client -> client.onGameStart());
        startNextPlayerTurn(false);
    }

    private void distributeHandCards() {
        Card[][] cards = deck.deal(clients.size());
        for (int i = 0; i < clients.size(); i++) {
            HandCards handcards = new HandCards(cardPatternEvaluator, cards[i]);
            // find the first player who holds club 3
            if (turn == -1 && ArrayUtils.contains(cards[i], CLUB3)) {
                turn = i;
            }
            Big2ClientContext clientContext = clients.get(i);
            handCardsMap.put(clientContext.getClient(), handcards);
        }
    }

    @Override
    public void playCard(CardGroup cardPlay) {
        Big2ClientContext context = getCurrentClientContext();
        Big2Client client = context.getClient();

        try {
            pass = 0;
            if (handCardsMap.get(getCurrentClient()).contains(cardPlay)) {
                playCard(cardPatternEvaluator.evaluate(cardPlay));
            } else {
                client.onCardPlayInvalid(cardPlay, context);
            }

        } catch (CardPatternInvalidException err) {
            client.onCardPlayInvalid(cardPlay, context);
        }
    }

    @Override
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
            Big2ClientContext context = getCurrentClientContext();
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

    @Override
    public void pass() {
        Player currentPlayer = getCurrentPlayer();
        boolean newRound = false;
        if (++pass  == clients.size()) {
            pass = 0;
            newRound = true;
            lastPlayPattern = null;
        }
        broadcast(ctx -> ctx.getClient().onPlayerPassed(currentPlayer, ctx));
        startNextPlayerTurn(newRound);
    }

    @Override
    public void talk(String msg) {
        messenger.talk(getCurrentPlayer().getName(), msg);
    }

    @Override
    public Big2Policy getBig2Policy() {
        return big2Policy;
    }

    /**
     * @param newRound whether all players have passed, the dominating player can start a new round,
     *                 i.e. the next player can play any card-pattern if set true.
     */
    private void startNextPlayerTurn(boolean newRound) {
        turn = turn + 1 >= clients.size() ? 0 : turn + 1;
        Big2Client client = getCurrentClient();
        client.onReceiveHandCards(client.getHandCards()), this);
        broadcast(ctx -> ctx.getClient().onPlayerTurn(context.getPlayer(), newRound, clients.get(turn)));
    }


    private void broadcast(Consumer<Big2Client> clientConsumer) {
        for (Big2Client client : clients) {
            clientConsumer.accept(client);
        }
    }

    private Player getCurrentPlayer() {
        return getCurrentClient().getPlayer();
    }

    private Big2Client getCurrentClient() {
        return clients.get(turn);
    }

    @Override
    public CardPattern getLastCardPlayPattern() {
        return lastPlayPattern;
    }

    @Override
    public boolean isNewRound() {
        return false;
    }
}
