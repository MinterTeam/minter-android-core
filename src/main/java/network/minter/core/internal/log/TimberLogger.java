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

import timber.log.Timber;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public final class TimberLogger extends Mint.Leaf {

    public TimberLogger(Timber.Tree tree) {
        // avoid double logging
        if (!Timber.forest().isEmpty()) {
            return;
        }
        Timber.plant(tree);
    }

    public TimberLogger() {
        // avoid double logging
        if (!Timber.forest().isEmpty()) {
            return;
        }
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void log(int priority, @Nullable String message, @Nullable Object... args) {
        String tag = getTag();
        if (tag != null) {
            Timber.tag(tag).log(priority, message, args);
        } else {
            Timber.log(priority, message, args);
        }

    }

    @Override
    public void log(int priority, @Nullable Throwable t, @Nullable String message, @Nullable Object... args) {
        final String tag = getTag();
        if (tag != null) {
            Timber.tag(tag).log(priority, t, message, args);
        } else {
            Timber.log(priority, t, message, args);
        }
    }
}
