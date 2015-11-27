package wfjv99.utility;

public class UnsignedInteger {

    public final int value;

    public UnsignedInteger(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Has to be non-negative");
        }
        this.value = value;
    }

    public UnsignedInteger(String binaryRepresentation) {
        if (binaryRepresentation.length() % 8 != 0 && binaryRepresentation.length() <= 32) {
            throw new IllegalArgumentException("Length must 1 to 4 bytes.");
        }

        StringBuilder reversedRepresentation = new StringBuilder(binaryRepresentation).reverse();

        int value = 0;
        for (int b = 0; b < reversedRepresentation.length(); b++) {
            if (reversedRepresentation.charAt(b) == '1') {
                value += Math.pow(2, b);
            }
        }

        this.value = value;
    }

    /**
     *
     * @param numberOfBytes size of the resulting unsigned integer in bytes
     * @return padded binary representation
     */
    public String asPaddedBinaryString(int numberOfBytes) {
        if (numberOfBytes <= 0) {
            throw new IllegalArgumentException("Positive number of bytes");
        }
        int numberOfBits = numberOfBytes * 8;
        StringBuilder unpaddedBinaryString = new StringBuilder();
        unpaddedBinaryString.append(Integer.toBinaryString(this.value));
        if (numberOfBits < unpaddedBinaryString.length()) {
            throw new IllegalArgumentException("This number (" + this.value + ") is to big to be represented within " + numberOfBytes + " bytes");
        }
        while (unpaddedBinaryString.length() != numberOfBits) {
            unpaddedBinaryString.insert(0, '0');
        }
        return unpaddedBinaryString.toString();
    }
}
