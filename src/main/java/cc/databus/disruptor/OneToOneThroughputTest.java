package cc.databus.disruptor;

import cc.databus.disruptor.util.MutableLong;
import cc.databus.disruptor.util.ValueAdditionEventHandler;
import cc.databus.disruptor.util.ValueEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.CountDownLatch;

public class OneToOneThroughputTest extends AbstractPerfTest {

    private static final int BUFFER_SIZE = 64 * 1024;
    private static final long ITERATIONS = 1000L * 1000L * 100L;
    private final long expectedResult = accumulatedAddition(ITERATIONS);

    private final ValueAdditionEventHandler handler = new ValueAdditionEventHandler();

    private final RingBuffer<ValueEvent> ringBuffer;

    private final MutableLong value = new MutableLong(0);

    public OneToOneThroughputTest() {
        Disruptor<ValueEvent> disruptor =
                new Disruptor<>(
                        ValueEvent.EVENT_FACTORY,
                        BUFFER_SIZE,
                        DaemonThreadFactory.INSTANCE,
                        ProducerType.SINGLE,
                        new YieldingWaitStrategy());
        disruptor.handleEventsWith(handler);
        this.ringBuffer = disruptor.start();
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected long runCase() throws InterruptedException {
        MutableLong value = this.value;
        final CountDownLatch latch = new CountDownLatch(1);
        long expectedCount = ringBuffer.getMinimumGatingSequence() + ITERATIONS;

        handler.reset(latch, expectedCount);

        long start = System.currentTimeMillis();

        final RingBuffer<ValueEvent> rb = ringBuffer;

        for (long l = 0 ; l < ITERATIONS; l++) {
            value.set(l);
            rb.publishEvent(Translator.INSTANCE, value);
        }

        latch.await();

        long opsPerSecond = (ITERATIONS * 1000L) / (System.currentTimeMillis() - start);
        waitForEventProcessorSequence(expectedCount);

        failIfNot(expectedResult, handler.getValue());

        return opsPerSecond;
    }

    private void waitForEventProcessorSequence(long expectedCount) throws InterruptedException {
        while (ringBuffer.getMinimumGatingSequence() != expectedCount) {
            Thread.sleep(1);
        }
    }

    private enum Translator implements EventTranslatorOneArg<ValueEvent, MutableLong> {

        INSTANCE;

        @Override
        public void translateTo(ValueEvent valueEvent, long l, MutableLong mutableLong) {
            valueEvent.setValue(mutableLong.get());
        }
    }


    public static void main(String[] args) throws Exception {
        new OneToOneThroughputTest().test();
    }
}
