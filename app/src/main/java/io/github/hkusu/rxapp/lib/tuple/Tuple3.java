package io.github.hkusu.rxapp.lib.tuple;

public class Tuple3<T1, T2, T3> {
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;

    private Tuple3(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public T1 get1() {
        return t1;
    }

    public T2 get2() {
        return t2;
    }

    public T3 get3() {
        return t3;
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> create(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }
}
