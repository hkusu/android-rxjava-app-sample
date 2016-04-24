package io.github.hkusu.rxapp.lib.tuple;

public class Tuple2<T1, T2> {
    private final T1 t1;
    private final T2 t2;

    private Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 get1() {
        return t1;
    }

    public T2 get2() {
        return t2;
    }

    public static <T1, T2> Tuple2<T1, T2> create(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }
}
