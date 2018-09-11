/*
 * Copyright (C) by MinterTeam. 2018
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.core;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import network.minter.core.crypto.BytesData;

import static junit.framework.TestCase.assertTrue;
import static network.minter.core.util.FastByteComparisons.equal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock[at]gmail.com]
 */
public class BytesDataTest {

    @Test
    public void testArrayConstructor() {
        final byte[] src = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] srcDropFirst = new byte[]{
                0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final ByteBuffer bufferBE = ByteBuffer.allocate(src.length).order(ByteOrder.BIG_ENDIAN);
        final ByteBuffer bufferLE = ByteBuffer.allocate(src.length).order(ByteOrder.LITTLE_ENDIAN);
        bufferBE.put(src);
        bufferLE.put(src);
        final BytesData data = new BytesData(src);

        assertTrue(data.isValid());

        assertTrue(data.equals(src));
        assertTrue(data.equals(bufferBE));
        assertTrue(data.equals(bufferLE));

        assertTrue(equal(data.getData(), src));
        assertTrue(equal(data.dropFirst(), srcDropFirst));
    }

    @Test
    public void testDropFirst() {
        final byte[] src = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] srcDropFirst = new byte[]{
                0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };

        final BytesData data = new BytesData(src);

        assertTrue(equal(data.dropFirst(), srcDropFirst));
    }

    @Test
    public void testDropFirstMutable() {
        final byte[] src = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] srcDropFirst = new byte[]{
                0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };

        final BytesData data = new BytesData(src);
        data.dropFirstMutable();

        assertTrue(data.equals(srcDropFirst));
    }

    @Test
    public void testDropLast() {
        final byte[] src = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] dropped = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00,
        };

        final BytesData data = new BytesData(src);

        assertTrue(equal(data.dropLast(), dropped));
    }

    @Test
    public void testDropLastMutable() {
        final byte[] src = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] dropped = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00,
        };

        final BytesData data = new BytesData(src);
        data.dropLastMutable();

        assertTrue(data.equals(dropped));
    }

    @Test
    public void testLpad() {
        final byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        final byte[] target = new byte[]{0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        final byte[] target2 = new byte[]{0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

        final BytesData data = new BytesData(src);
        assertTrue(equal(data.lpad(5), target));
        assertTrue(equal(data.lpad(6), target2));
    }

    @Test
    public void testTakeFirst() {
        final byte[] src = new byte[]{(byte) 0xFA, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF};
        final byte[] target = new byte[]{(byte) 0xFA, (byte) 0xFE};

        final BytesData data = new BytesData(src);
        assertTrue(equal(data.takeFirst(2), target));

        Throwable t = null;
        try {
            assertTrue(equal(data.takeFirst(6), target));
        } catch (IndexOutOfBoundsException e) {
            t = e;
        }

        assertNotNull(t);
    }

    @Test
    public void testTakeFirstMutable() {
        final byte[] src = new byte[]{(byte) 0xFA, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF};
        final byte[] target = new byte[]{(byte) 0xFA, (byte) 0xFE};

        final BytesData data = new BytesData(src);
        data.takeFirstMutable(2);
        assertTrue(data.equals(target));

        Throwable t = null;
        try {
            data.takeFirstMutable(6);
        } catch (IndexOutOfBoundsException e) {
            t = e;
        }

        assertNotNull(t);
    }

    @Test
    public void testGetDataMutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src);
        final byte[] target = data.getData();
        assertEquals((byte) 0xFF, target[0]);
        assertEquals((byte) 0xFF, data.getData()[0]);
        target[0] = (byte) 0xAA;
        assertEquals((byte) 0xAA, target[0]);
        assertEquals((byte) 0xAA, data.getData()[0]);
    }

    @Test
    public void testGetDataMutableFromImmutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src, true);

        assertEquals((byte) 0xFF, src[0]);
        assertEquals((byte) 0xFF, data.getData()[0]);
        src[0] = (byte) 0xAA;
        assertEquals((byte) 0xAA, src[0]);
        assertEquals((byte) 0xFF, data.getData()[0]);
    }

    @Test
    public void testGetDataImmutableFromImmutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src, true);

        assertEquals((byte) 0xFF, src[0]);
        assertEquals((byte) 0xFF, data.getDataImmutable()[0]);
        src[0] = (byte) 0xAA;
        assertEquals((byte) 0xAA, src[0]);
        assertEquals((byte) 0xFF, data.getDataImmutable()[0]);
    }

    @Test
    public void testGetDataImmutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src);
        final byte[] target = data.getDataImmutable();
        assertEquals((byte) 0xFF, target[0]);
        assertEquals((byte) 0xFF, data.getDataImmutable()[0]);
        target[0] = (byte) 0xAA;
        assertEquals((byte) 0xAA, target[0]);
        assertEquals((byte) 0xFF, data.getDataImmutable()[0]);
    }

    @Test
    public void testTakeLastMutable() {
        final byte[] src = new byte[]{(byte) 0xFA, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF};
        final byte[] target = new byte[]{(byte) 0xFF, (byte) 0xFF};

        final BytesData data = new BytesData(src);
        data.takeLastMutable(2);
        assertTrue(data.equals(target));

        Throwable t = null;
        try {
            data.takeLastMutable(6);
        } catch (IndexOutOfBoundsException e) {
            t = e;
        }

        assertNotNull(t);
    }

    @Test
    public void testTakeLast() {
        final byte[] src = new byte[]{(byte) 0xFA, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF};
        final byte[] target = new byte[]{(byte) 0xFF, (byte) 0xFF};

        final BytesData data = new BytesData(src);
        assertTrue(equal(data.takeLast(2), target));

        Throwable t = null;
        try {
            assertTrue(equal(data.takeLast(6), target));
        } catch (IndexOutOfBoundsException e) {
            t = e;
        }

        assertNotNull(t);
    }

    @Test
    public void testCleanup() {
        final byte[] src = new byte[]{
                (byte) 0xFF, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        final byte[] cleaned = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00
        };
        final BytesData data = new BytesData(src);
        data.cleanup();
        assertFalse(data.isValid());
        // after cleanup, equals method will always return false
        assertFalse(data.equals(cleaned));
    }
}
