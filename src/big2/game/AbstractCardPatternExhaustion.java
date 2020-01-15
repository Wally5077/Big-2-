package big2.game;

import big2.game.patterns.CardPattern;
import big2.game.patterns.CardPatternExhaustion;
import big2.game.policies.CardPatternPolicy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractCardPatternExhaustion implements CardPatternExhaustion {
    protected CardPatternPolicy cardPatternPolicy;
    protected Map<Class<? extends CardPattern>, List<CardPattern>> cardPatternMap = new HashMap<>();

    public AbstractCardPatternExhaustion(List<CardPattern> cardPatterns, CardPatternPolicy cardPatternPolicy) {
        this.cardPatternPolicy = cardPatternPolicy;
        setupCardPatternMap(cardPatterns);
    }

    private void setupCardPatternMap(List<CardPattern> cardPatterns) {
        for (CardPattern cardPattern : cardPatterns) {
            if (!cardPatternMap.containsKey(cardPattern.getClass())) {
                cardPatternMap.put(cardPattern.getClass(), new LinkedList<>());
            }
            cardPatternMap.get(cardPattern.getClass()).add(cardPattern);
        }
    }

    @Override
    public List<CardPattern> getCardPatternsByType(Class<? extends CardPattern> type) {
        return cardPatternMap.get(type);
    }

}
