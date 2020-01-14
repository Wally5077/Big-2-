package big2.game.patterns;

import big2.cards.Card;
import big2.cards.CardGroup;
import big2.game.policies.CardPatternPolicyAdapter;
import big2.game.policies.CardPolicy;
import big2.utils.CardUtils;

import java.util.List;
import java.util.Set;

public class FullHouseCardPatternEvaluatorAdapter implements CardPatternEvaluatorAdapter {

    @Override
    public boolean isMatched(CardGroup cardGroup, CardPolicy cardPolicy) {
        List<CardGroup> cardGroups = cardGroup.divideByRank();
        return cardGroups.size() == 2 &&
                Math.max(cardGroups.get(0).size(), cardGroups.get(1).size()) == 3 &&
                Math.min(cardGroups.get(0).size(), cardGroups.get(1).size()) == 2;
    }

    @Override
    public FullHouseCardPattern create(CardGroup cardGroup, CardPolicy policy) {
        List<CardGroup> cardGroups = cardGroup.divideByRank();
        return new FullHouseCardPattern(policy, cardGroups.get(0).getCards(),
                cardGroups.get(1).getCards());
    }

    @Override
    public Set<FullHouseCardPattern> enumerateCardPatterns(CardGroup cards, CardPolicy cardPolicy) {
        return null;  //TODO
    }

    public static class FullHouseCardPattern extends AbstractCardPattern {
        private Card[] threeCardsPart;
        private Card[] twoCardsPart;

        public FullHouseCardPattern(CardPolicy cardPolicy, Card[] threeCardsPart, Card[] twoCardsPart) {
            super(cardPolicy, threeCardsPart, twoCardsPart);
            this.threeCardsPart = threeCardsPart;
            this.twoCardsPart = twoCardsPart;
        }

        @Override
        public int getLevel() {
            return CardUtils.findMaxLevel(cardPolicy, threeCardsPart);
        }

        public Card[] getThreeCardsPart() {
            return threeCardsPart;
        }

        public Card[] getTwoCardsPart() {
            return twoCardsPart;
        }
    }

}
