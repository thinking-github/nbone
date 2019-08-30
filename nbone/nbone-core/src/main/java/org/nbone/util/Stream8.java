package org.nbone.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * java 8  stream Utils
 *
 * @author thinking
 * @version 1.0
 * @since 2019-08-27
 */
public class Stream8 {

    /**
     * collection to map
     *
     * @return
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection,
                                         Function<? super T, ? extends K> keyMapper) {

        return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    /**
     * collection to map
     *
     * @return
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection,
                                         Function<? super T, ? extends K> keyMapper,
                                         BinaryOperator<T> mergeFunction) {

        return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity(), mergeFunction));
    }

    /**
     * collection to map
     *
     * @return
     */
    public static <T, K, U> Map<K, U> toMap(Collection<T> collection,
                                            Function<? super T, ? extends K> keyMapper,
                                            Function<? super T, ? extends U> valueMapper) {

        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * collection to map ,key重复时取新值
     *
     * @return
     */
    public static <T, K, U> Map<K, U> toMapNew(Collection<T> collection,
                                               Function<? super T, ? extends K> keyMapper,
                                               Function<? super T, ? extends U> valueMapper) {

        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, (oldValue, newValue) -> newValue));
    }

    /**
     * collection to map ,key重复时取老的值
     *
     * @return
     */
    public static <T, K, U> Map<K, U> toMapOld(Collection<T> collection,
                                               Function<? super T, ? extends K> keyMapper,
                                               Function<? super T, ? extends U> valueMapper) {

        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, (oldValue, newValue) -> oldValue));
    }

    public static void main(String args[]){
        List names = new ArrayList();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);
    }


}
