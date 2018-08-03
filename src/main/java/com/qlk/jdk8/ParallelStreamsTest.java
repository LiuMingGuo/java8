package com.qlk.jdk8;

import java.util.stream.LongStream;

public class ParallelStreamsTest {
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    public static void main(String[] args) {
        long n=1000_000_000;
        Long start=System.currentTimeMillis();
        System.out.println("rangedSum result"+rangedSum(n));
        System.out.println("rangedSum time:"+(+System.currentTimeMillis()-start));
        start=System.currentTimeMillis();
        System.out.println("parallelRangedSum result"+ parallelRangedSum(n));
        System.out.println("parallelRangedSum time:"+(+System.currentTimeMillis()-start));
//        for (int i=1;i<10;i++)
//        System.out.println(sideEffectSum(100));
    }
}
class Accumulator {
    public long total = 0;
    public void add(long value) { total += value; }
}
