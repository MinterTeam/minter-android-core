package network.minter.mintercore.internal.common;

/**
 * Provides instances of {@code T}
 * <p>
 * <p>
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface CallbackProvider<T> {

    T get();
}
