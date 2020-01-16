package big2.game;

import big2.cards.Deck;
import big2.game.patterns.*;
import big2.game.policies.*;

import java.util.List;
import java.util.Objects;

public class Big2GameBuilder {
	private Deck deck = null;
	private boolean sortedDeck = false;
	private int deckShuffleTime = 3;
	private CardPolicy cardPolicy = new StandardCardPolicy();

	//TODO provide a way to construct the orderedCardPatternPolicy
	private StandardCardPatternPolicy orderedCardPatternPolicy = new StandardCardPatternPolicy(cardPolicy);
	private CardPlayPolicy cardPlayPolicy = new SamePatternPlayPolicy(orderedCardPatternPolicy);
	private Messenger messenger = new SystemOutMessenger();
	private List<CardPatternAdapter> cardPatternAdapters = StandardCardPatternAdapters.get();

	public Big2GameBuilder deck(Deck deck) {
		this.deck = Objects.requireNonNull(deck);
		return this;
	}

	public Big2GameBuilder sortedDeck() {
		this.sortedDeck = true;
		return this;
	}

	public Big2GameBuilder shuffledDeck(int shuffleTimes) {
		if (shuffleTimes < 0) {
			throw new IllegalArgumentException("The shuffle times should be positive, given " +shuffleTimes + ".");
		}
		this.deckShuffleTime = shuffleTimes;
		return this;
	}

	public Big2GameBuilder cardPolicy(CardPolicy policy) {
		this.cardPolicy =  Objects.requireNonNull(policy);
		return this;
	}

	public Big2GameBuilder freePatternPolicy() {
		cardPlayPolicy(new FreePatternPlayPolicy(orderedCardPatternPolicy));
		return this;
	}

	public Big2GameBuilder cardPlayPolicy(CardPlayPolicy policy) {
		this.cardPlayPolicy =  Objects.requireNonNull(policy);
		return this;
	}

	public Big2GameBuilder setMessenger(Messenger messenger) {
		this.messenger = messenger;
		return this;
	}

	public Big2GameBuilder registerCardPatternEvaluatorAdapter(CardPatternAdapter adapter) {
		cardPatternAdapters.add(adapter);
		return this;
	}

	public Big2Game build() {
		Big2Policy big2Policy = new CompositeBig2Policy(cardPolicy, orderedCardPatternPolicy, cardPlayPolicy);
		CardPatternFacade evaluator = new CardPatternFacade(big2Policy, cardPatternAdapters);

		if (deck == null) {
			deck = new Deck();
			if (!sortedDeck) {
				deck.shuffle(deckShuffleTime);
			}
		}
		return new Big2Game(messenger, evaluator, big2Policy, deck);
	}

}
