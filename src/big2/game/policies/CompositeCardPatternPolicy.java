package big2.game.policies;

import big2.game.patterns.CardPattern;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeCardPatternPolicy implements CardPatternPolicy {
    private HashMap<Class<? extends CardPattern>, CardPatternPolicyAdapter> cardPatternPolicyMap = new HashMap<>();
    private TreeSet<CardPatternPolicyAdapter<? extends CardPattern>> adapterTreeSet =
            new TreeSet<>(Comparator.comparingInt(CardPatternPolicyAdapter::getCardPatternBaseLevel));

    public void addCardPatternPolicyAdapter(CardPatternPolicyAdapter<? extends CardPattern>
                                                    cardPatternPolicyAdapter) {
        cardPatternPolicyMap.put(cardPatternPolicyAdapter.getCardPatternType(), cardPatternPolicyAdapter);
        adapterTreeSet.add(cardPatternPolicyAdapter);
    }

    @Override
    public int getTotalLevel(CardPattern cardPattern) {
        return cardPatternPolicyMap.getOrDefault(cardPattern.getClass(),
                NullCardPatternPolicyAdapter.getInstance())
                .getTotalLevel(cardPattern);
    }

    @Override
    public int getCardPatternBaseLevel(Class<? extends CardPattern> type) {
        return  cardPatternPolicyMap.getOrDefault(type,
                NullCardPatternPolicyAdapter.getInstance())
                .getCardPatternBaseLevel();
    }

    @Override
    public Collection<Class<? extends CardPattern>> getCeilingLevelCardPatternTypes(Class<? extends CardPattern> type) {
        SortedSet<CardPatternPolicyAdapter<? extends CardPattern>> v = adapterTreeSet.tailSet(null);
        return adapterTreeSet.tailSet(cardPatternPolicyMap.get(type)).stream();

    }

    @Override
    public Collection<Class<? extends CardPattern>> getFloorLevelCardPatternTypes(Class<? extends CardPattern> type) {
        return adapterTreeSet.headSet(cardPatternPolicyMap.get(type))
                .stream().map(CardPatternPolicyAdapter::getCardPatternType).collect(Collectors.toList());
    }

}
