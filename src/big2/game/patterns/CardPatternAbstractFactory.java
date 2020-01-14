package big2.game.patterns;

import big2.game.policies.CardPatternPolicyAdapter;

public interface CardPatternAbstractFactory<T extends CardPattern> {
    CardPatternEvaluatorAdapter<T> createEvaluatorAdapter();
    CardPatternPolicyAdapter<T> createPolicyAdapter();
}
