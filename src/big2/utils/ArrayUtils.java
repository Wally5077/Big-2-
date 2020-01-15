package big2.utils;

import big2.cards.Card;
import big2.cards.Rank;
import big2.cards.Suit;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntFunction;

public class ArrayUtils {

    public static boolean contains(Object[] objects, Object obj) {
        for (Object object : objects) {
            if (object.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public static void swap(Object[] objects, int index1, int index2) {
        Object temp = objects[index1];
        objects[index1] = objects[index2];
        objects[index2] = temp;
    }

    public static <T> List<T[]> cartesianProduct(IntFunction<T[]> arrayFactory, T[] left, T[] right) {
        LinkedList<T[]> product = new LinkedList<>();
        for (T t1 : left) {
            for (T t2 : right) {
                T[] pair = arrayFactory.apply(2);
                pair[0] = t1;
                pair[1] = t2;
                product.add(pair);
            }
        }
        return product;
    }

    public static <T> List<T[]> combination(final int length, IntFunction<T[]> arrayFactory, final T[] candidates) {
        List<T[]> enumeration = new LinkedList<>();
        if (candidates.length < length) {
            return Collections.emptyList();
        }
        return combination(enumeration, arrayFactory, length, 0, 0, new boolean[candidates.length], candidates);
    }

    public static <T> List<T[]> combination(final List<T[]> enumeration, IntFunction<T[]> arraySupplier,
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
            combination(enumeration, arraySupplier, length, curLength+1, curIdx+1, member, candidates);
            member[curIdx] = false;
            combination(enumeration, arraySupplier, length, curLength, curIdx+1, member, candidates);
        }
        return enumeration;
    }

    public static void main(String[] args) {
        List<Card[]> cardCombination = combination(2, Card[]::new,
                new Card[]{new Card(Rank.A, Suit.CLUB),
                        new Card(Rank.R2, Suit.CLUB),
                        new Card(Rank.R3, Suit.CLUB)});

        for (Card[] cards : cardCombination) {
            System.out.println(Arrays.toString(cards));
        }

        for (Integer[] nums : cartesianProduct(Integer[]::new, new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6})) {
            System.out.println(Arrays.toString(nums));
        }
    }
}
