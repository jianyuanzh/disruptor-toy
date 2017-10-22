package cc.databus.disruptor.util;

public class PaddedLong extends MutableLong {
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddedLong() {
    }

    public PaddedLong(long value) {
        super(value);
    }

    public long sumPaddingToPreventOptimisation() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}
