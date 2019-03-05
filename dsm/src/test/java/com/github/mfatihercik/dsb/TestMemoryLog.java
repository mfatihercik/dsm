package com.github.mfatihercik.dsb;

public class TestMemoryLog {
    private long beforeMemory = 0;
    private long afterMemory = 0;

    public static void gc() {
        Runtime.getRuntime().gc();
    }

    public static long bytesToMegabytes(long memory) {
        return (memory / (1024 * 1024));
    }

    public void logBefore() {
        beforeMemory = calculateMemory();
    }

    private long calculateMemory() {
        Runtime runtime = Runtime.getRuntime();
        return bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
    }

    public void logAfter() {
        afterMemory = calculateMemory();

    }

    public void print() {
        System.out.println("Before Memory: " + beforeMemory);
        System.out.println("After Memory: " + afterMemory);
        System.out.println("Used Memory: " + (afterMemory - beforeMemory));

    }

}
