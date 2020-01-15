package big2.game.policies;

import big2.game.patterns.CardPattern;
import big2.utils.CardUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;

public class OrderedCardPatternPolicy implements CardPatternPolicy {
    protected LinkedList<Class<? extends CardPattern>> cardPatternTypeList = new LinkedList<>();
    private CardPolicy cardPolicy;

    public OrderedCardPatternPolicy(CardPolicy cardPolicy) {
        this.cardPolicy = cardPolicy;
    }

    public void addFirst(Class<? extends CardPattern> insertedType) {
        cardPatternTypeList.addFirst(insertedType);
    }

    public void addLast(Class<? extends CardPattern> insertedType) {
        cardPatternTypeList.addLast(insertedType);
    }

    public void insertFront(Class<? extends CardPattern> targetType,
                            Class<? extends CardPattern> insertedType) {
        int indexOfTarget = cardPatternTypeList.indexOf(targetType);
        if (indexOfTarget == cardPatternTypeList.size() - 1) {
            cardPatternTypeList.addLast(insertedType);
        } else {
            cardPatternTypeList.add(indexOfTarget + 1, insertedType);
        }
    }

    public void insertBehind(Class<? extends CardPattern> targetType,
                             Class<? extends CardPattern> insertedType) {
        cardPatternTypeList.add(cardPatternTypeList.indexOf(targetType), insertedType);
    }

    @Override
    public int compare(CardPattern c1, CardPattern c2) {
        int idx1 = cardPatternTypeList.indexOf(c1.getClass());
        int idx2 = cardPatternTypeList.indexOf(c2.getClass());
        if (idx1 != idx2) {
            return idx1 - idx2;
        }
        if (c1.equals(c2)) {
            return 0;
        }
        if (c1.getLevel() == c2.getLevel()) {
            return 1;  //if two different patterns have the same level, arbitrarily return 1.
        }
        return c1.getLevel() - c2.getLevel();
    }

    @Override
    public int compare(Class<? extends CardPattern> type1, Class<? extends CardPattern> type2) {
        return cardPatternTypeList.indexOf(type1) - cardPatternTypeList.indexOf(type2);
    }

    @Override
    public NavigableSet<Class<? extends CardPattern>> getOrderedCardPatternTypes() {
        TreeSet<Class<? extends CardPattern>> orderedTypes =
                new TreeSet<>(Comparator.comparingInt(c -> cardPatternTypeList.indexOf(c)));
        orderedTypes.addAll(cardPatternTypeList);
        return orderedTypes;
    }
}
