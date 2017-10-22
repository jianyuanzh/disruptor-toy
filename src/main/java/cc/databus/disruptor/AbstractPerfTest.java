package cc.databus.disruptor;

public abstract class AbstractPerfTest {



    protected abstract long runCase() throws Exception;

    protected void test() throws Exception {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (availableProcessors < getRequiredProcessors()) {
            System.out.print("*** Warning ***: your system has insufficient processors to execute the test efficiently. ");
            System.out.println("Processors required = " + getRequiredProcessors() + " available = " + availableProcessors);
        }

        int runs = getRuns();
        long[] ops = new long[runs];

        System.out.println("Start running cases");
        for (int i = 0; i < runs; i++) {
            System.gc();
            ops[i] = runCase();
            System.out.format("Run %d, operations %,d ops/sec%n", i, ops[i]);
        }
    }

    protected int getRuns() {
        return 7;
    }

    protected int getRequiredProcessors() {
        return  2;
    }

    static void failIfNot(long a, long b) {
        if (a != b) {
            throw new RuntimeException(" value not equal " + a + " != " + b);
        }
    }

    static long accumulatedAddition(final long iterations) {
        long tmp = 0L;
        for (long i = 0L; i < iterations; i++) {
            tmp += i;
        }

        return tmp;
    }
}
