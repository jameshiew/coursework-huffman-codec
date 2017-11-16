package wfjv99.huffman;

import wfjv99.utility.ASCIIToBitstreamWriter;
import wfjv99.utility.Counter;
import wfjv99.utility.UnsignedInteger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Encoder {

    public static void main(String[] args) {
        // check correct number of arguments
        if (args.length != 3) {
            System.err.println("ARGUMENTS: [path to file to be encoded] [number of bytes per block] [path to write out to]");
        }

        try {
            // ensure filepath specified is a valid path and is accessible
            Path filepath = Paths.get(args[0]);
            // ensure block size specified is a non-negative integer
            long blockSize = Long.parseLong(args[1]);
            // if so, run encoding
            Encoder encoder = new Encoder(filepath, blockSize);
            encoder.encode(Paths.get(args[2]));
        } catch (InvalidPathException invalidPathException) {
            System.err.println("The path '" + args[0] + "' could not be parsed as a valid file path.");
            System.exit(1);
        } catch (NumberFormatException numberFormatException) {
            System.err.println("Could not parse a block size from '" + args[1] + "', please enter a positive integer.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error during encoding: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private final Path filepath;
    private final long blockSize;

    public Encoder(Path filepath, long blockSize){
        if (filepath == null) {
            throw new IllegalArgumentException();
        }
        this.filepath = filepath;

        if (blockSize <= 0) {
            throw new IllegalArgumentException("Block size must be at least one.");
        }
        this.blockSize = blockSize;
    }

    public final void encode(Path outpath) throws IOException {
        if (outpath == null) {
            throw new IllegalArgumentException();
        }

        // count blocks from the stream
        Counter<Block> blockCounter = new Counter<Block>();

        InputStream in = null;
        try {
            in = Files.newInputStream(filepath);
            BlockReader blockReader = new BlockReader(in, blockSize);
            blockCounter.countFrom(blockReader);
        } finally {
            in.close();
        }

        HuffmanTree root = HuffmanTree.fromBlockCounter(blockCounter);

        // generate encodings
        Map<Block, String> encodings = EncodingBuilder.getEncodings(root);

        ASCIIToBitstreamWriter out = null;
        in = null;
        try {
            in = Files.newInputStream(filepath);
            out = new ASCIIToBitstreamWriter(Files.newOutputStream(outpath));

            // number of bytes per block (1 byte)
            out.write(new UnsignedInteger((int) blockSize).asPaddedBinaryString(1));
            // number of blocks (4 bytes)
            int numberOfBlocks = encodings.size();
            out.write(new UnsignedInteger(numberOfBlocks).asPaddedBinaryString(4));
            // write out Huffman tree
            out.write(root.toString());

            // write out encoding
            BlockReader blockReader = new BlockReader(in, blockSize);
            for (Block block : blockReader) {
                String encoding = encodings.get(block);
                assert encoding != null;
                out.write(encoding);
            }
        } finally {
            in.close();
            out.close();
        }

    }
}
