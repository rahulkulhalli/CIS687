package org.syr.cis687.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonUtils {
    public static <T> List<T> convertIterableToList(Iterable<T> obj) {
        List<T> list = new ArrayList<>();
        for (T x: obj) {
            list.add(x);
        }

        return list;
    }
}
