package wfjv99.utility;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Write ASCII 1's and 0's to a bitstream.
 *
 * Flushing an instance of this class writes out any remaining bits to be
 * written and pads with 0s.
 */
public class ASCIIToBitstreamWriter implements Flushable, Closeable {

    private final OutputStream out;
    // current byte being built up
    private String currentByte;

    public ASCIIToBitstreamWriter(OutputStream out) {
        if (out == null) {
            throw new IllegalArgumentException();
        }
        this.out = out;
        this.currentByte = "";
    }

    public void write(String asciiOnesAndZeroes) throws IOException {
        assert 0 <= this.currentByte.length() && this.currentByte.length() < 8;
        if (asciiOnesAndZeroes == null) {
            throw new IllegalArgumentException();
        }
        while (asciiOnesAndZeroes.length() != 0) {
            int numberOfBitsYetNeeded = 8 - this.currentByte.length();
            if (numberOfBitsYetNeeded <= asciiOnesAndZeroes.length()) {
                this.currentByte += asciiOnesAndZeroes.substring(0, numberOfBitsYetNeeded);
                asciiOnesAndZeroes = asciiOnesAndZeroes.substring(numberOfBitsYetNeeded);
                this.writeOut();
            } else {
                this.currentByte += asciiOnesAndZeroes;
                asciiOnesAndZeroes = "";
            }
        }
        assert 0 <= this.currentByte.length() && this.currentByte.length() < 8;
    }

    private void writeOut() throws IOException {
        assert this.currentByte.length() == 8;
        this.out.write(ASCIIBitstring.get(this.currentByte));
        this.currentByte = "";
    }

    public int bitsLeftUntilWholeByte() {
        return 8 - this.currentByte.length();
    }

    /**
     * pad the output stream with zeros so its a whole number of bytes
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        while (this.currentByte.length() != 8) {
            this.currentByte += "0";
        }
        this.out.write(ASCIIBitstring.get(currentByte));
        this.currentByte = "";
    }

    @Override
    public void close() throws IOException {
        this.flush();
        this.out.close();
    }
}