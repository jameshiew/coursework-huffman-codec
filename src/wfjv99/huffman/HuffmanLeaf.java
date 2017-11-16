package wfjv99.huffman;

import wfjv99.utility.ASCIIBitstring;

public class HuffmanLeaf implements HuffmanNode {

    public final Block block;
    public final int frequency;

    public HuffmanLeaf(Block block, int frequency) {
        if (block == null) {
            throw new IllegalArgumentException();
        }
        this.block = block;

        if (frequency <= 0) {
            throw new IllegalArgumentException();
        }
        this.frequency = frequency;
    }

    @Override
    public int frequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "1" + this.block;
    }
}
