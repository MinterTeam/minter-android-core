/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
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
package network.minter.core.util;

import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DecodeResult implements Serializable {

    private int pos;
    private Object decoded;

    public DecodeResult(int pos, Object decoded) {
        this.pos = pos;
        this.decoded = decoded;
    }

    public int getPos() {
        return pos;
    }

    public Object getDecoded() {
        return decoded;
    }

    public String toString() {
        return asString(this.decoded);
    }

    private String asString(Object decoded) {
        if (decoded instanceof String) {
            return (String) decoded;
        } else if (decoded instanceof byte[]) {
            return Hex.toHexString((byte[]) decoded);
        } else if (decoded instanceof Object[]) {
            StringBuilder sb = new StringBuilder();
            for (Object item : (Object[]) decoded) {
                sb.append(asString(item));
            }
            return sb.toString();
        }
        throw new RuntimeException("Not a valid type. Should not occur");
    }
}
