package network.minter.bipwallet.internal.common;

/**
 * Provides instances of {@code T}
 *
 *
 * Dogsy. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface CallbackProvider<T> {

    T get();
}
