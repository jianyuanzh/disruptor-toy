package cc.databus.disruptor.util;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.CountDownLatch;

public class ValueAdditionEventHandler implements EventHandler<ValueEvent> {

    private final PaddedLong value = new PaddedLong();
    private long count;
    private CountDownLatch latch;

    public void reset(final CountDownLatch latch, final long expectedCount) {
        value.set(0L);
        this.latch = latch;
        count = expectedCount;
    }

    @Override
    public void onEvent(ValueEvent valueEvent, long sequence, boolean endOfBatch) throws Exception {
        value.set(value.get() + valueEvent.getValue());

        if (count == sequence) {
            latch.countDown();
        }
    }

    public long getValue() {
        return value.get();
    }
}
