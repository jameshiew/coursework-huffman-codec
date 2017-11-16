package wfjv99.huffman;

import wfjv99.utility.ASCIIBitstring;
import wfjv99.utility.BitstreamToASCIIReader;
import wfjv99.utility.UnsignedInteger;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Decoder {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("ARGUMENTS: [path to encoded file] [path to write out to]");
            System.exit(1);
        }

        try {
            Path filepath = Paths.get(args[0]);
            new Decoder(filepath).decode(Paths.get(args[1]));
        } catch (InvalidPathException invalidPathException) {
            System.err.println("The path '" + args[0] + "' could not be parsed as a valid file path.");
            System.exit(1);
        }
    }

    private final Path filepath;

    public Decoder(Path filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException();
        }
        this.filepath = filepath;
    }

    public final void decode(Path pathToWriteOutTo) throws IOException {
        if (pathToWriteOutTo == null) {
            throw new IllegalArgumentException();
        }
        // check outpath doesn't exist...

        OutputStream out = null;
        BitstreamToASCIIReader in = null;
        try {
            in = new BitstreamToASCIIReader(Files.newInputStream(this.filepath));
            out = Files.newOutputStream(pathToWriteOutTo);
            int blockSize = ASCIIBitstring.get(in.read(8));
            long numberOfBlocks = new UnsignedInteger(in.read(32)).value;
            // read in number of branches + number of leaves + all bits for blocks
//            System.out.println("" + numberOfBlocks + " * " + blockSize + "-byte blocks.");

            String huffmanTree = in.read((int) (numberOfBlocks - 1 + numberOfBlocks + numberOfBlocks * blockSize * 8));
//            System.out.println(dehydratedTree);

            HuffmanTree root = HuffmanTree.fromString(huffmanTree, blockSize);
            Map<Block, String> encodings = EncodingBuilder.getEncodings(root);

            Map<String, Block> decodings = new HashMap<String, Block>();
            for (Block block : encodings.keySet()) {
                // hack because for some reason an extra zero is prepended to the codes
                decodings.put(encodings.get(block).substring(1), block);
//                System.out.println("" + block + ": " + encodings.get(block));
            }

            assert encodings.size() == numberOfBlocks;

            int maximumCodeLength = EncodingBuilder.getLongestValidCode(encodings).length();


            StringBuilder current = new StringBuilder();
            try {
                while (true) {
                    // read in bits and decode
                    current.append(in.read(1));
                    if (decodings.containsKey(current.toString())) {
                        Block block = decodings.get(current.toString());
                        for (Integer _byte : block) {
                            out.write(_byte);
                        }
                        current = new StringBuilder();
                    } else if (current.length() == maximumCodeLength) {
                        throw new IOException("Unknown code encountered: " + current);
                    }
                }
            } catch (EOFException e) {

            }
        } finally {
            in.close();
            out.close();
        }
    }
}
