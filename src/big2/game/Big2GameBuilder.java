package big2.game;

import big2.game.patterns.*;
import big2.game.policies.*;

import java.util.LinkedList;
import java.util.List;

public class Big2GameBuilder {
	private CardPolicy cardPolicy = new StandardCardPolicy();
	private OrderedCardPatternPolicy orderedCardPatternPolicy = new OrderedCardPatternPolicy();
	private CardPlayPolicy cardPlayPolicy = new SamePatternPlayPolicy(orderedCardPatternPolicy);
	private Messenger messenger = new SystemOutMessenger();
	private List<CardPatternEvaluatorAdapter> evaluatorAdapters = new LinkedList<>();

	public Big2GameBuilder cardPolicy(CardPolicy policy) {
		this.cardPolicy = policy;
		return this;
	}

	public Big2GameBuilder freePatternPolicy() {
		cardPlayPolicy(new FreePatternPlayPolicy(orderedCardPatternPolicy));
		return this;
	}

	public Big2GameBuilder cardPlayPolicy(CardPlayPolicy policy) {
		this.cardPlayPolicy = policy;
		return this;
	}

	public Big2GameBuilder setMessenger(Messenger messenger) {
		this.messenger = messenger;
		return this;
	}

	public Big2GameBuilder registerCardPatternEvaluatorAdapter(CardPatternEvaluatorAdapter adapter) {
		evaluatorAdapters.add(adapter);
		return this;
	}

	public Big2Game build() {
		Big2Policy big2Policy = new CompositeBig2Policy(cardPolicy, orderedCardPatternPolicy, cardPlayPolicy);
		return new Big2Game(messenger,
				new CardPatternEvaluator(big2Policy, evaluatorAdapters), big2Policy);
	}

}
