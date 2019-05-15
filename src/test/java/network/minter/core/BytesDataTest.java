/*
 * Copyright (C) by MinterTeam. 2019
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
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.util.FastByteComparisons;

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
    public void testArrayArrayConstructor() {
        final byte[][] a = new byte[3][2];
        a[0] = new byte[]{0xF, 0xF - 1};
        a[1] = new byte[]{0xF - 2, 0xF - 3};
        a[2] = new byte[]{0xF - 4, 0xF - 5};

        BytesData bd = new BytesData(a);
        assertEquals(a.length * 2, bd.size());
        // flatten
        assertEquals(0xF, bd.getData()[0]);
        assertEquals(0xF - 1, bd.getData()[1]);
        assertEquals(0xF - 2, bd.getData()[2]);
        assertEquals(0xF - 3, bd.getData()[3]);
        assertEquals(0xF - 4, bd.getData()[4]);
        assertEquals(0xF - 5, bd.getData()[5]);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Test
    public void testArrayArrayConstructorVaArgs() {
        BytesData bd = new BytesData(
                new byte[]{0xF - 0, 0xF - 1},
                new byte[]{0xF - 2, 0xF - 3},
                new byte[]{0xF - 4, 0xF - 5}
        );
        assertEquals(3 * 2, bd.size());
        // flatten
        assertEquals(0xF, bd.getData()[0]);
        assertEquals(0xF - 1, bd.getData()[1]);
        assertEquals(0xF - 2, bd.getData()[2]);
        assertEquals(0xF - 3, bd.getData()[3]);
        assertEquals(0xF - 4, bd.getData()[4]);
        assertEquals(0xF - 5, bd.getData()[5]);
    }

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
    public void testSha256ImplicitlyImmutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src);

        byte[] hash = data.sha256();
        // cause current constructor is not immutable
        assertFalse(FastByteComparisons.equal(hash, src));
        // the same
        assertFalse(FastByteComparisons.equal(hash, data.getData()));
    }

    @Test
    public void testSha256ExplicitlyMutable() {
        byte[] src = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        BytesData data = new BytesData(src);

        byte[] before = data.getData();
        data.sha256Mutable();
        // hashed data saved only inside BytesData, src didn't touched
        assertFalse(FastByteComparisons.equal(data.getData(), src));
        // and even copied data before hashing
        assertFalse(FastByteComparisons.equal(data.getData(), before));
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

    @Test
    public void testDropFirstZeroes() {
        // 1
        final byte[] src1 = new byte[]{
                (byte) 0xFF, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        byte[] target1 = BytesHelper.dropFirstZeroes(src1);
        assertEquals(src1.length, target1.length);
        assertTrue(BytesHelper.equals(src1, target1));

        // 2
        final byte[] src2 = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        byte[] target2 = BytesHelper.dropFirstZeroes(src2);
        assertEquals(20, src2.length);
        assertEquals(1, target2.length);
        assertTrue(BytesHelper.equals(new byte[]{(byte) 0xFF}, target2));

        // 3
        final byte[] src3 = new byte[]{
                0x00, (byte) 0xAA, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0xFF
        };
        byte[] target3 = BytesHelper.dropFirstZeroes(src3);
        assertEquals(20, src3.length);
        assertEquals(19, target3.length);
        assertEquals((byte) 0xAA, target3[0]);
        assertEquals((byte) 0xFF, target3[target3.length - 1]);
    }
}
