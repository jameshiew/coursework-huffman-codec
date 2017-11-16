package wfjv99.huffman;

import wfjv99.utility.ASCIIBitstring;
import wfjv99.utility.UnsignedInteger;

import java.util.LinkedList;

/**
 * Represents a block of bytes.
 */
public class Block extends LinkedList<Integer> {

    public void add(String unsignedBinaryRepresentation) {
        int value = new UnsignedInteger(unsignedBinaryRepresentation).value;
        if (0 <= value && value < 256) {
            this.add(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * ASCII bitstring representation.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Integer _byte : this) {
            s.append(ASCIIBitstring.get(_byte));
        }
        return s.toString();
    }
}
