package wfjv99.huffman;

import wfjv99.utility.ASCIIBitstring;
import wfjv99.utility.Counter;

import java.util.*;

public class HuffmanTree implements HuffmanNode {

    public HuffmanNode left;
    public HuffmanNode right;

    public static HuffmanTree fromString(String representation, int blockSize) {
        return HuffmanTree.fromString(new StringBuilder(representation), blockSize);
    }

    public static HuffmanTree fromString(StringBuilder representation, int blockSize) {
        Stack<HuffmanTree> previous = new Stack<HuffmanTree>();
        Set<HuffmanTree> finished = new HashSet<HuffmanTree>();
        HuffmanTree root = new HuffmanTree();
        HuffmanTree current = root;
        while (representation.length() != 0) {
            // seek to branch which has an empty child
            while (current.left != null && current.right != null) {
                if (current.right instanceof HuffmanTree && !(finished.contains(current.right))) {
                    previous.push(current);
                    current = (HuffmanTree) current.right;
                } else {
                    finished.add(current);
                    current = previous.pop();
                }
            }

            char c = representation.charAt(0);
            representation.deleteCharAt(0);
            if (c == '0') {
                if (current.left == null) {
                    current.left = new HuffmanTree();
                    previous.push(current);
                    current = (HuffmanTree) current.left;
                } else {
                    assert current.right == null;
                    current.right = new HuffmanTree();
                    previous.push(current);
                    current = (HuffmanTree) current.right;
                }
            } else {
                assert c == '1';
                Block block = new Block();
                for (int i = 0; i < blockSize; i++) {
                    block.add(ASCIIBitstring.get(representation.substring(0, 8)));
                    representation.delete(0, 8);
                }
                HuffmanLeaf leaf = new HuffmanLeaf(block, 1);
                if (current.left == null) {
                    current.left = leaf;
                } else {
                    assert current.right == null;
                    current.right = leaf;
                }
            }
        }
        return root;
    }

    private HuffmanTree() {

    }

    public static HuffmanTree fromBlockCounter(Counter<Block> blockCounter) {
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<HuffmanNode>(11, new Comparator<HuffmanNode>() {
            @Override
            public int compare(HuffmanNode nodeA, HuffmanNode nodeB) {
                return nodeA.frequency() <= nodeB.frequency() ? -1 : +1;
            }
        });

        // put block-frequency leaves into pq
        for (Block block : blockCounter.seen()) {
            HuffmanLeaf leaf = new HuffmanLeaf(block, blockCounter.get(block));
            nodes.add(leaf);
        }
        assert nodes.size() == blockCounter.size();

        // start building branches and re-adding them to the priority queue as they are built
        while (2 <= nodes.size()) {
            HuffmanNode treeA = nodes.poll();
            HuffmanNode treeB = nodes.poll();
            HuffmanTree parent = new HuffmanTree(treeA, treeB);
            nodes.add(parent);
        }

        // grab root of Huffman tree
        assert nodes.size() == 1;
        HuffmanTree root = (HuffmanTree) nodes.poll();
        assert root != null;
        return root;
    }

    public HuffmanTree(HuffmanNode left, HuffmanNode right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException();
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public int frequency() {
        return this.left.frequency() + this.right.frequency();
    }

    @Override
    public String toString() {
        return "0" + this.left + this.right;
    }
}
