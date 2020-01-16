package big2.game;

import big2.cards.*;
import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternEvaluator;
import big2.game.patterns.CardPatternInvalidException;
import big2.game.policies.Big2Policy;
import big2.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Big2Game implements Big2ClientContext {
    private final static Card CLUB3 = new Card(Rank.R3, Suit.CLUB);
    private Deck deck;
    private Messenger messenger;
    private CardPatternEvaluator cardPatternEvaluator;
    private Big2Policy big2Policy;

    /**
     * whether all players have passed, the dominating player can start a new round,
     *   i.e. the next player can play any card-pattern if set true.
     */
    private boolean newRound;
    private CardPattern lastPlayPattern;
    private ArrayList<Big2Client> clients = new ArrayList<>(4);
    private HashMap<Big2Client, Player> clientPlayerMap = new HashMap<>(4);
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
        clients.add(client);
    }

    public void start() {
        setupPlayers();
        distributeHandCards();
        turn = findFirstPlayerIndexWhoHoldsClub3();
        broadcast(client -> client.onGameStart(clientPlayerMap.get(client)));
        startNextPlayerTurn();
    }

    private void setupPlayers() {
        for (int i = 0; i < clients.size(); i ++) {
            Big2Client client = clients.get(i);
            String name = client.getName();
            Player player = new Player(i, name);
            clientPlayerMap.put(client, player);
        }
    }

    private void distributeHandCards() {
        Card[][] cards = deck.deal(clients.size());
        for (int i = 0; i < clients.size(); i++) {
            HandCards handcards = new HandCards(cardPatternEvaluator, cards[i]);
            Big2Client client = clients.get(i);
            clientPlayerMap.get(client).setHandCards(handcards);
        }
    }

    private int findFirstPlayerIndexWhoHoldsClub3() {
        for (int i = 0; i < clients.size(); i++) {
            HandCards handCards = clientPlayerMap.get(clients.get(i)).getHandCards();
            if (ArrayUtils.contains(handCards.getCards(), CLUB3)) {
                return i;
            }
        }
        throw new IllegalStateException("Implementation Error, club 3 not found.");
    }

    @Override
    public void playCard(CardGroup cardPlay) {
        Big2Client client = getCurrentClient();
        try {
            pass = 0;
            if (getPlayer(client).containsHandCards(cardPlay)) {
                playCard(cardPatternEvaluator.evaluate(cardPlay));
            } else {
                client.onCardPlayInvalid(cardPlay, this);
            }
        } catch (CardPatternInvalidException err) {
            client.onCardPlayInvalid(cardPlay, this);
        }
    }

    @Override
    public void playCard(CardPattern cardPlayPattern) {
        overrideLastPlayPatternIfValid(cardPlayPattern);
        startNextPlayerTurnOrGameOver();
    }

    private void overrideLastPlayPatternIfValid(CardPattern cardPlayPattern) {
        if (newRound || big2Policy.isValidPlay(lastPlayPattern, cardPlayPattern)) {
            lastPlayPattern = cardPlayPattern;
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.removeHandCards(cardPlayPattern.getCards());
            broadcast(client -> client.onNewCardPlay(currentPlayer, cardPlayPattern, this));
        } else {
            getCurrentClient().onCardPlayInvalid(new CardGroup(cardPlayPattern.getCards()), this);
        }
    }

    private void startNextPlayerTurnOrGameOver() {
        if (!isGameOver()) {
            startNextPlayerTurn();
        } else {
            broadcast(client -> client.onGameOver(getCurrentPlayer(), messenger));
        }
    }

    private boolean isGameOver() {
        return getCurrentPlayer().hasEmptyHandCards();
    }

    @Override
    public void pass() {
        Player currentPlayer = getCurrentPlayer();
        newRound = false;
        if (++pass  == clients.size()) {
            pass = 0;
            newRound = true;
            lastPlayPattern = null;
        }
        broadcast(client -> client.onPlayerPassed(currentPlayer, this));
        startNextPlayerTurn();
    }

    @Override
    public void talk(String msg) {
        messenger.talk(getCurrentPlayer().getName(), msg);
    }

    @Override
    public Big2Policy getBig2Policy() {
        return big2Policy;
    }

    private void startNextPlayerTurn() {
        turn = turn + 1 >= clients.size() ? 0 : turn + 1;
        Big2Client currentClient = getCurrentClient();
        Player currentPlayer = getPlayer(currentClient);
        currentClient.onReceiveHandCards(currentPlayer.getHandCards(), this);
        for (Big2Client client : clients) {
            client.onPlayerTurn(client == currentClient,
                    currentPlayer, newRound, this);
        }
    }


    private void broadcast(Consumer<Big2Client> clientConsumer) {
        for (Big2Client client : clients) {
            clientConsumer.accept(client);
        }
    }

    private Player getCurrentPlayer() {
        return getPlayer(getCurrentClient());
    }

    private Player getPlayer(Big2Client client) {
        return clientPlayerMap.get(client);
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
        return newRound;
    }
}
