package big2.game;

import big2.game.patterns.*;
import big2.game.policies.*;

import java.util.LinkedList;
import java.util.List;

public class Big2GameBuilder {
	private CardPolicy cardPolicy = new StandardCardPolicy();
	private CompositeCardPatternPolicy cardPatternPolicy = new CompositeCardPatternPolicy();
	private CardPlayPolicy cardPlayPolicy = new SamePatternPlayPolicy(cardPatternPolicy);
	private Messenger messenger = new SystemOutMessenger();
	private List<CardPatternEvaluatorAdapter> evaluatorAdapters = new LinkedList<>();

	public Big2GameBuilder() {
		registerCardPatternAbstractFactory(new SingleCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new PairCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new FullHouseCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new StraightCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new FourOfRankCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new FlushCardPatternAbstractFactory());
		registerCardPatternAbstractFactory(new StraightFlushCardPatternAbstractFactory());
	}

	public Big2GameBuilder cardPolicy(CardPolicy policy) {
		this.cardPolicy = policy;
		return this;
	}

	public Big2GameBuilder freePatternPolicy() {
		cardPlayPolicy(new FreePatternPlayPolicy(cardPatternPolicy));
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

	public Big2GameBuilder registerCardPatternAbstractFactory(CardPatternAbstractFactory factory) {
		registerCardPatternPolicyAdapter(factory.createPolicyAdapter());
		registerCardPatternEvaluatorAdapter(factory.createEvaluatorAdapter());
		return this;
	}

	public Big2GameBuilder registerCardPatternPolicyAdapter(CardPatternPolicyAdapter policyAdapter) {
		this.cardPatternPolicy.addCardPatternPolicyAdapter(policyAdapter);
		return this;
	}


	public Big2GameBuilder registerCardPatternEvaluatorAdapter(CardPatternEvaluatorAdapter adapter) {
		evaluatorAdapters.add(adapter);
		return this;
	}

	public Big2Game build() {
		Big2Policy big2Policy = new CompositeBig2Policy(cardPolicy, cardPatternPolicy, cardPlayPolicy);
		return new Big2Game(messenger,
				new CardPatternEvaluator(big2Policy, evaluatorAdapters), big2Policy);
	}

}
