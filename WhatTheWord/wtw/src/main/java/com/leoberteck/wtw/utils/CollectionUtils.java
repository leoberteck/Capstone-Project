package com.leoberteck.wtw.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CollectionUtils {
    private static final Random random = new Random();

    private static  <T> T getRandomPosition(T[] array) {
        return array[random.nextInt(array.length)];
    }

    public static  <T> T getRandomPosition(Collection<T> collection) {
        return getRandomPosition((T[])collection.toArray());
    }

    public static List<Integer> range(int start, int end){
        List<Integer> range = new ArrayList<>();
        while (start <= end) {
            range.add(start++);
        }
        return range;
    }
}
