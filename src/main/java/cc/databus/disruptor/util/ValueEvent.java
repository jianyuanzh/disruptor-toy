package cc.databus.disruptor.util;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public static final EventFactory<ValueEvent> EVENT_FACTORY = ValueEvent::new;
}
