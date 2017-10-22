package cc.databus.disruptor.util;

/**
 * A holder for a long value
 */
public class MutableLong {
    private long value = 0L;

    /**
     * Default constructor
     */
    public MutableLong() {
    }

    /**
     * Constructor with initial value.
     *
     * @param value initially set.
     */
    public MutableLong(long value) {
        this.value = value;
    }

    /**
     * Get the long value.
     *
     * @return the long value.
     */
    public long get() {
        return value;
    }

    /**
     * Set the long value.
     *
     * @param value to set.
     */
    public void set(final long value) {
        this.value = value;
    }

    /**
     * Increase the value by 1.
     */
    public void increment() {
        value++;
    }
}
