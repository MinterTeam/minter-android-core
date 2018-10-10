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

import javax.annotation.Nullable;

/**
 * Mint - Minter Logger
 * <p>
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public abstract class Mint {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;
    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;
    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;
    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;
    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    private static Leaf sLog;

    /**
     * Set log delegate
     * @param leaf can be null to disable logging
     * @param <T> type extends {@link Leaf}
     */
    public static <T extends Leaf> void brew(T leaf) {
        sLog = leaf;
    }

    public static Leaf tag(String tag) {
        sLog.explicitTag.set(tag);
        return sLog;
    }

    /** Log at {@code priority} a message with optional format args. */
    public static void log(int priority, String message, Object... args) {
        if (sLog == null) {
            return;
        }

        sLog.log(priority, message, args);
    }

    /** Log at {@code priority} a message with optional format args and throwable. */
    public static void log(int priority, Throwable t, String message, Object... args) {
        if (sLog == null) {
            return;
        }

        sLog.log(priority, t, message, args);
    }

    public static void v(String message, Object... args) {
        log(VERBOSE, message, args);
    }

    public static void v(Throwable t, String message, Object... args) {
        log(VERBOSE, t, message, args);
    }

    public static void v(Throwable t) {
        log(VERBOSE, t, null);
    }

    public static void d(String message, Object... args) {
        log(DEBUG, message, args);
    }

    public static void d(Throwable t, String message, Object... args) {
        log(DEBUG, t, message, args);
    }

    public static void d(Throwable t) {
        log(DEBUG, t, null);
    }

    public static void i(String message, Object... args) {
        log(INFO, message, args);
    }

    public static void i(Throwable t, String message, Object... args) {
        log(INFO, t, message, args);
    }

    public static void i(Throwable t) {
        log(INFO, t, null);
    }

    public static void w(String message, Object... args) {
        log(WARN, message, args);
    }

    public static void w(Throwable t, String message, Object... args) {
        log(WARN, message, args);
    }

    public static void w(Throwable t) {
        log(WARN, t, null);
    }

    public static void e(String message, Object... args) {
        log(ERROR, message, args);
    }

    public static void e(Throwable t, String message, Object... args) {
        log(ERROR, t, message, args);
    }

    public static void e(Throwable t) {
        log(ERROR, t, null);
    }

    public static void wtf(String message, Object... args) {
        log(ASSERT, message, args);
    }

    public static void wtf(Throwable t, String message, Object... args) {
        log(ASSERT, t, message, args);
    }

    public static void wtf(Throwable t) {
        log(ASSERT, t, null);
    }

    public static abstract class Leaf {
        private final ThreadLocal<String> explicitTag = new ThreadLocal<>();

        /** Log at {@code priority} a message with optional format args. */
        public abstract void log(int priority, String message, Object... args);

        /** Log at {@code priority} a message with optional format args and throwable. */
        public abstract void log(int priority, Throwable t, String message, Object... args);

        public void v(String message, Object... args) {
            log(VERBOSE, message, args);
        }

        public void v(Throwable t, String message, Object... args) {
            log(VERBOSE, t, message, args);
        }

        public void v(Throwable t) {
            log(VERBOSE, t, null);
        }

        public void d(String message, Object... args) {
            log(DEBUG, message, args);
        }

        public void d(Throwable t, String message, Object... args) {
            log(DEBUG, t, message, args);
        }

        public void d(Throwable t) {
            log(DEBUG, t, null);
        }

        public void i(String message, Object... args) {
            log(INFO, message, args);
        }

        public void i(Throwable t, String message, Object... args) {
            log(INFO, t, message, args);
        }

        public void i(Throwable t) {
            log(INFO, t, null);
        }

        public void w(String message, Object... args) {
            log(WARN, message, args);
        }

        public void w(Throwable t, String message, Object... args) {
            log(WARN, message, args);
        }

        public void w(Throwable t) {
            log(WARN, t, null);
        }

        public void e(String message, Object... args) {
            log(ERROR, message, args);
        }

        public void e(Throwable t, String message, Object... args) {
            log(ERROR, t, message, args);
        }

        public void e(Throwable t) {
            log(ERROR, t, null);
        }

        public void wtf(String message, Object... args) {
            log(ASSERT, message, args);
        }

        public void wtf(Throwable t, String message, Object... args) {
            log(ASSERT, t, message, args);
        }

        public void wtf(Throwable t) {
            log(ASSERT, t, null);
        }

        @Nullable
        protected final String getTag() {
            String tag = explicitTag.get();
            if (tag != null) {
                explicitTag.remove();
            }
            return tag;
        }
    }
}
