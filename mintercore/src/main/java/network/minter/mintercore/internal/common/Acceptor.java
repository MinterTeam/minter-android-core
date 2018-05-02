package network.minter.mintercore.internal.common;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public interface Acceptor<T> {
    void accept(T t);
}
