package big2.game.policies;

import big2.game.patterns.CardPattern;

public class NullCardPatternPolicyAdapter implements CardPatternPolicyAdapter {
    private static NullCardPatternPolicyAdapter instance = new NullCardPatternPolicyAdapter();

    public static NullCardPatternPolicyAdapter getInstance() {
        return instance;
    }

    @Override
    public Class<? extends CardPattern> getCardPatternType() {
        return CardPattern.class;
    }

    @Override
    public int getCardPatternBaseLevel() {
        return 0;
    }

    @Override
    public int getTotalLevel(CardPattern cardPattern) {
        return 0;
    }

}
