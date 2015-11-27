package  wfjv99.utility;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BitstreamToASCIIReader implements Closeable {

    private final InputStream in;
    private String currentByte;
    private int haveOutputtedUpToButNotIncludingIndex;
    private int totalBytesRead;

    public BitstreamToASCIIReader(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException();
        }
        this.in = in;
        this.haveOutputtedUpToButNotIncludingIndex = 0;
        this.totalBytesRead = 0;
    }

    private int bitsLeftToOutputFromCurrentByte() {
        return 8 - this.haveOutputtedUpToButNotIncludingIndex + 1;
    }

    public String read(int numberOfBits) throws IOException {
        StringBuilder s = new StringBuilder();
        // if no byte has ever been read in, read in an initial one
        if (this.currentByte == null) {
            this.currentByte = ASCIIBitstring.get(this.in.read());
            this.totalBytesRead++;
        }
        // build up the string that will be returned with read in bits
        while (s.length() < numberOfBits) {
            s.append(this.currentByte.charAt(haveOutputtedUpToButNotIncludingIndex));
            this.haveOutputtedUpToButNotIncludingIndex++;
            this.haveOutputtedUpToButNotIncludingIndex %= 8;
            if (this.haveOutputtedUpToButNotIncludingIndex == 0) {
                int newByte = this.in.read();
                if (newByte == -1) {
                    this.close();
                    throw new EOFException("End of file reached after " + this.totalBytesRead + " bytes.");
                }
                this.totalBytesRead++;
                this.currentByte = ASCIIBitstring.get(newByte);
            }
        }
        return s.toString();
    }

    public static void main(String[] args) throws IOException {
        // quick function to convert file to ASCII bitstring and print to stdout
        BitstreamToASCIIReader b = new BitstreamToASCIIReader(Files.newInputStream(Paths.get(args[0])));
        while (true) {
            System.out.print(b.read(8));
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}