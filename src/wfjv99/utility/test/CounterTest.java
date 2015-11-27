package wfjv99.utility.test;

import static org.junit.Assert.*;

import org.junit.Test;
import wfjv99.utility.Counter;

import java.util.HashSet;
import java.util.Set;

public class CounterTest {

    @Test
    public void shouldRecordCountOfMoreThanOne() {
        Counter<String> counter = new Counter<String>();
        counter.increment("hello");
        counter.increment("hello");
        counter.increment("hello");
        assertEquals(counter.get("hello"), 3);
    }

    @Test
    public void shouldSeeMoreThanOneElement() {
        Counter<String> counter = new Counter<String>();
        counter.increment("hello");
        counter.increment("goodbye");
        assertEquals(counter.seen().size(), 2);
    }

    @Test
    public void shouldCountFromIterable() {
        Counter<String> counter = new Counter<String>();
        Set<String> strings = new HashSet<String>();
        strings.add("foo");
        strings.add("bar");
        strings.add("baz");
        counter.countFrom(strings);
        for (String string : strings) {
            assertEquals(counter.get(string), 1);
        }
        assertEquals(counter.seen().size(), 3);
    }

}
