package network.minter.bipwallet.internal.exceptions;

import network.minter.my.models.MyResult;

/**
 * Dogsy. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class ResponseException extends RuntimeException {
    private MyResult.Error mError;

    public ResponseException(MyResult.Error error) {
        mError = error;
    }

    public ResponseException(MyResult<?> result) {
        this(result.getError());
    }

    public MyResult.Error getError() {
        return mError;
    }
}
