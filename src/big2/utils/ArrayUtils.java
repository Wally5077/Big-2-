package big2.utils;

import big2.cards.Card;
import big2.cards.Rank;
import big2.cards.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class ArrayUtils {

    public static boolean contains(Object[] objects, Object obj) {
        for (Object object : objects) {
            if (object.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T[]> enumerate(final int length, IntFunction<T[]> arrayFactory, final T[] candidates) {
        List<T[]> enumeration = new LinkedList<>();
        return enumerate(enumeration, arrayFactory, length, 0, 0, new boolean[candidates.length], candidates);
    }

    public static <T> List<T[]> enumerate(final List<T[]> enumeration, IntFunction<T[]> arraySupplier,
                                           final int length, final int curLength, final int curIdx,
                                           final boolean[] member, final T[] candidates) {
        if (length == curLength) {
            T[] objs = arraySupplier.apply(length);
            int idx = 0;
            for (int i = 0; i < member.length; i++) {
                if (member[i]) {
                    objs[idx++] = candidates[i];
                }
            }
            enumeration.add(objs);
        } else if (curIdx < member.length){
            member[curIdx] = true;
            enumerate(enumeration, arraySupplier, length, curLength+1, curIdx+1, member, candidates);
            member[curIdx] = false;
            enumerate(enumeration, arraySupplier, length, curLength, curIdx+1, member, candidates);
        }
        return enumeration;
    }

    public static void main(String[] args) {
        List<Card[]> cardCombination = enumerate(2, Card[]::new,
                new Card[]{new Card(Rank.A, Suit.CLUB),
                        new Card(Rank.R2, Suit.CLUB),
                        new Card(Rank.R3, Suit.CLUB)});

        for (Card[] cards : cardCombination) {
            System.out.println(Arrays.toString(cards));
        }
    }
}
