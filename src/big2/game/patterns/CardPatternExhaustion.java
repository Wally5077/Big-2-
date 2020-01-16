package big2.game.patterns;

import java.util.List;
import java.util.function.Consumer;

public interface CardPatternExhaustion {
    List<CardPattern> getCardPatternsByType(Class<? extends CardPattern> type);

    boolean containCardPatternType(Class type);

    CardPatternOptional lowerCardPattern(CardPattern cardPattern);

    CardPatternOptional higherCardPattern(CardPattern cardPattern);

    CardPatternOptional ceilingCardPattern(CardPattern cardPattern);

    CardPatternOptional floorCardPattern(CardPattern cardPattern);

    CardPattern pollFirstCardPattern();

    CardPattern pollLastCardPattern();


    interface CardPatternOptional {
        CardPatternOptional doOrElse = cardPatternConsumer -> Runnable::run;
        OrElse emptyOrElse = runnable -> {};

        static CardPatternOptional ofNullable(CardPattern cardPattern) {
            return cardPattern == null ? doOrElse : cardPatternConsumer -> {
                cardPatternConsumer.accept(cardPattern);
                return emptyOrElse;
            };
        }

        OrElse ifPresent(Consumer<CardPattern> cardPatternConsumer);

        interface OrElse {
            void orElse(Runnable runnable);
        }
    }
}
