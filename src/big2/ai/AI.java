package big2.ai;

import big2.game.Big2Client;
import big2.game.Big2ClientContext;
import big2.game.Player;
import big2.game.patterns.CardPattern;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class AI implements Big2Client {

    @Override
    public void onPlayerTurn(boolean isYourTurn, Player player, boolean newRound, Big2ClientContext context) {
        List<CardPattern> validOptions = player.getHandCards().exhaustCardPatterns()
                                        .stream()
                                        .filter(context::isValidPlay)
                                        .collect(Collectors.toList());
        onPlayerTurn(isYourTurn, player, newRound, validOptions, context);
    }

    protected abstract void onPlayerTurn(boolean isYourTurn, Player player, boolean newRound,
                                         List<CardPattern> validOptions, Big2ClientContext context);
}
