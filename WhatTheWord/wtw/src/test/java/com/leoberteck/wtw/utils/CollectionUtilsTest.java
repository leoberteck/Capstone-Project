package com.leoberteck.wtw.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CollectionUtilsTest {

    @Test
    public void range() {
        List<Integer> list = CollectionUtils.range(0, 10);
        for(Integer x=0, count =list.size();x< count;x++) {
            Assert.assertEquals(x, list.get(x));
        }
    }
}