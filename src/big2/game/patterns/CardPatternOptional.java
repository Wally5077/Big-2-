package big2.game.patterns;

import java.util.function.Consumer;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface CardPatternOptional {
    CardPatternOptional doOrElse = cardPatternConsumer -> Runnable::run;
    CardPatternOptional.OrElse emptyOrElse = runnable -> {};

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