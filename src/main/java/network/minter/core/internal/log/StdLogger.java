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

package network.minter.core.internal.log;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import static network.minter.core.internal.common.Preconditions.firstNonNull;
import static network.minter.core.internal.log.Mint.ASSERT;
import static network.minter.core.internal.log.Mint.DEBUG;
import static network.minter.core.internal.log.Mint.ERROR;
import static network.minter.core.internal.log.Mint.INFO;
import static network.minter.core.internal.log.Mint.VERBOSE;
import static network.minter.core.internal.log.Mint.WARN;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class StdLogger extends Mint.Leaf {
    private PrintStream mPrinter;
    private PrintStream mErrPrinter;

    public StdLogger() {
        mPrinter = System.out;
        mErrPrinter = System.err;
    }

    public StdLogger(PrintStream out, PrintStream err) {
        mPrinter = out;
        mErrPrinter = err;
    }

    @Override
    public void log(int priority, String message, Object... args) {
        final String tag = getTag();
        String printableTag = "";

        if (tag != null) {
            printableTag = String.format("%s: ", printableTag);
        }

        switch (priority) {
            case VERBOSE:
            case DEBUG:
            case INFO:
            case WARN:
                if (args.length == 0) {
                    out().println(String.format("%s%s", printableTag, message));
                } else {
                    out().println(String.format("%s%s", printableTag, String.format(Locale.getDefault(), message, args)));
                }
                break;
            case ERROR:
            case ASSERT:
                if (args.length == 0) {
                    err().println(String.format("%s%s", printableTag, message));
                } else {
                    err().println(String.format("%s%s", printableTag, String.format(Locale.getDefault(), message, args)));
                }
                break;
        }
    }

    @Override
    public void log(int priority, Throwable t, String message, Object... args) {
        final String tag = getTag();
        String printableTag = "";

        if (tag != null) {
            printableTag = String.format("%s: ", printableTag);
        }

        switch (priority) {
            case VERBOSE:
            case DEBUG:
            case INFO:
            case WARN:
                if (t == null) {
                    if (args.length == 0) {
                        out().println(String.format("%s%s", printableTag, message));
                    } else {
                        out().println(String.format("%s%s", printableTag, String.format(Locale.getDefault(), message, args)));
                    }
                } else {
                    String st = extractStackTrace(t);
                    if (args.length == 0) {
                        out().println(String.format("%s%s\n%s", printableTag, message, st));
                    } else {
                        out().println(String.format("%s%s\n%s", printableTag, st, String.format(Locale.getDefault(), message, args)));
                    }
                }

                break;
            case ERROR:
            case ASSERT:
                if (t == null) {
                    if (args.length == 0) {
                        err().println(String.format("%s%s", printableTag, message));
                    } else {
                        err().println(String.format("%s%s", printableTag, String.format(Locale.getDefault(), message, args)));
                    }
                } else {
                    String st = extractStackTrace(t);
                    if (args.length == 0) {
                        err().println(String.format("%s%s\n%s", printableTag, message, st));
                    } else {
                        err().println(String.format("%s%s\n%s", printableTag, st, String.format(Locale.getDefault(), message, args)));
                    }
                }
                break;

        }
    }

    protected String extractStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    private PrintStream out() {
        return firstNonNull(mPrinter, System.out);
    }

    private PrintStream err() {
        return firstNonNull(mErrPrinter, System.err);
    }
}
