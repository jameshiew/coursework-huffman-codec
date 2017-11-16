package wfjv99.huffman;

import java.util.HashMap;
import java.util.Map;

public class EncodingBuilder {

    private static void build(Map<Block, String> cipher, String currentEncoding, HuffmanTree branch) {
        if (branch.left instanceof HuffmanTree) {
            build(cipher, currentEncoding + "0", (HuffmanTree) branch.left);
        } else if (branch.left instanceof HuffmanLeaf) {
            cipher.put(((HuffmanLeaf) branch.left).block, currentEncoding + "0");
        }

        if (branch.right instanceof HuffmanTree) {
            build(cipher, currentEncoding + "1", (HuffmanTree) branch.right);
        } else if (branch.right instanceof HuffmanLeaf) {
            cipher.put(((HuffmanLeaf) branch.right).block, currentEncoding + "1");
        }
    }

    public static Map<Block, String> getEncodings(HuffmanTree root) {
        Map<Block, String> encodings = new HashMap<Block, String>();
        EncodingBuilder.build(encodings, "", root);
        return encodings;
    }

    public static String getLongestValidCode(Map<Block, String> encodings) {
        String longestValidCode = "";
        for (String code : encodings.values()) {
            if (longestValidCode.length() < code.length()) {
                longestValidCode = code;
            }
        }
        assert !(longestValidCode.equals(""));
        return longestValidCode;
    }
}
