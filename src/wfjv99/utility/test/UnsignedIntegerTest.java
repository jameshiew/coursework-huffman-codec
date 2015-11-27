package wfjv99.utility.test;

import static org.junit.Assert.*;
import org.junit.Test;
import wfjv99.utility.UnsignedInteger;

public class UnsignedIntegerTest {

    @Test
    public void shouldConvertBinaryRepresentations() {
        UnsignedInteger seventySeven = new UnsignedInteger("01001101");
        assertEquals(seventySeven.value, 77);
        UnsignedInteger six = new UnsignedInteger("00000000000000000000000000000110");
        assertEquals(six.value, 6);
    }

    @Test
    public void shouldGiveValidUnsignedBinaryRepresentation() {
        UnsignedInteger oneHundredAndNinetyThree = new UnsignedInteger(193);
        assertEquals(oneHundredAndNinetyThree.asPaddedBinaryString(1), "11000001");
        assertEquals(oneHundredAndNinetyThree.asPaddedBinaryString(2), "0000000011000001");
    }
}
