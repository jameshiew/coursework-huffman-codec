package wfjv99.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Converts from a byte stream to a block stream of set block size.
 */
public class BlockReader implements Iterator<Block>, Iterable<Block> {

    // the bytestream which is being read from
    private final InputStream in;
    // the size of blocks to produce
    public final long blockSize;

    public BlockReader(InputStream in, long blockSize) {
        super();
        if (in == null) {
            throw new IllegalArgumentException();
        }
        try {
            in.available();
        } catch (IOException ioException) {
            throw new IllegalArgumentException("Input stream cannot be read from.");
        }
        this.in = in;

        if (blockSize <= 0) {
            throw new IllegalArgumentException("Block size must be at least 1.");
        }
        this.blockSize = blockSize;
    }

    @Override
    public boolean hasNext() {
        try {
            // check if stream has bytes left to read
            return this.in.available() != 0;
        } catch (IOException ioException) {
            // if stream is closed
            return false;
        }
    }

    @Override
    public Block next() {
        Block block = new Block();
        try {
            for (int i = 0; i < this.blockSize; i++) {
                block.add(in.read());
            }
        } catch (IOException ioException) {
            throw new NoSuchElementException("Not enough bytes left in stream to make a new block.");
        }
        assert block.size() == this.blockSize;
        return block;
    }

    @Override
    public void remove() {
        // not implemented
        return;
    }

    @Override
    public Iterator<Block> iterator() {
        return this;
    }
}
