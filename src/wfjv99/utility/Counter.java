package wfjv99.utility;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple counter class.
 */
public class Counter<T> {

    private final Map<T, Integer> count;

    public Counter() {
        super();
        this.count = new HashMap<T, Integer>(16);
    }

    public void countFrom(Iterable<T> iterable) {
        if (iterable == null) {
            throw new IllegalArgumentException();
        }
        for (T t : iterable) {
            this.increment(t);
        }
    }

    public void increment(T t) {
        if (this.count.containsKey(t)) {
            int currentCount = this.count.get(t);
            this.count.put(t, currentCount + 1);
        } else {
            this.count.put(t, 1);
        }
    }

    public Collection<T> seen() {
        return Collections.unmodifiableSet(this.count.keySet());
    }

    public int size() {
        return this.count.size();
    }

    public int get(T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        return this.count.containsKey(t) ? this.count.get(t) : 0;
    }

    /**
     *
     * @return the count of the most frequent element
     */
    public int maxCount() {
        int maxFrequency = 0;
        for (Integer frequency : this.count.values()) {
            if (maxFrequency < frequency) {
                maxFrequency = frequency;
            }
        }
        return maxFrequency;
    }

    public int totalCount() {
        int accumulator = 0;
        for (Integer elementCount : this.count.values()) {
            accumulator += elementCount;
        }
        return accumulator;
    }
}
