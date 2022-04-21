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

package network.minter.core.internal.exceptions;

import java.io.EOFException;
import java.io.IOError;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import retrofit2.HttpException;
import retrofit2.Response;

import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class NetworkException extends RuntimeException {

    private final static ArrayList<Class<? extends Throwable>> friendExceptions = new ArrayList<Class<? extends Throwable>>() {{
        add(TimeoutException.class);
        add(SocketTimeoutException.class);
        add(HttpException.class);
        add(UnknownServiceException.class);
        add(UnknownHostException.class);
        add(ConnectException.class);
        add(EOFException.class);
        add(IOException.class);
        add(IOError.class);
    }};
    private final Code mStatusCode;
    private String mUserMessage;
    private String mExceptionMessage;
    private String mUrl;
    private Response<?> mResponse = null;
    private Throwable another = null;

    public enum Code {
        BadRequest(400),
        Unauthorized(401),
        Forbidden(403),
        NotFound(404),
        MethodNotAllowed(405),
        InternalServerError(500),
        BadGateway(502),
        ServiceUnavailable(503),
        ServiceGatewayTimedOut(504),
        Unknown(1000),
        TimedOut(1001),
        UnknownHost(1002),
        UnknownService(1003),
        HostUnreachable(1004),
        UnexpectedEOF(1005),
        UnexpectedIO(1006),
        ;

        private final int code;

        Code(int c) {
            code = c;
        }

        static Code fromStatusCode(int statusCode) {
            for (Code code : Code.values()) {
                if (code.code == statusCode) {
                    return code;
                }
            }

            return Unknown;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "ERR::%s::%d", name().toUpperCase(), code);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public NetworkException(Throwable another) {
        this.another = another;

        if (another instanceof TimeoutException || another instanceof SocketTimeoutException) {
            mStatusCode = Code.TimedOut;
            mUserMessage = "Connection timed out";
            mExceptionMessage = another.getMessage();
        } else if (another instanceof UnknownHostException) {
            mStatusCode = Code.UnknownHost;
            mUserMessage = "Bad internet connection";
            mExceptionMessage = another.getMessage();
        } else if (another instanceof UnknownServiceException) {
            mStatusCode = Code.UnknownService;
            mUserMessage = "Service unavailable. Please, try again later";
            mExceptionMessage = another.getMessage();
        } else if (another instanceof HttpException) {
            // deprecated метод наследует новый, поэтому кастим к нему
            mStatusCode = Code.fromStatusCode(((HttpException) another).code());

            switch (mStatusCode) {
                case BadGateway:
                case ServiceUnavailable:
                    mUserMessage = "Server unavailable...";
                    break;
                case InternalServerError:
                    mUserMessage = "Server error. Please, try again later";
                    break;
                case ServiceGatewayTimedOut:
                    mUserMessage = "Server is not responding. Please, try again later";
                    break;
                case Forbidden:
                case Unauthorized:
                    mUserMessage = "Authorization error";
                    break;
                default:
                    mUserMessage = "DecodeError error. Please, try again later";
            }

            mExceptionMessage = another.getMessage();
            mResponse = ((HttpException) another).response();
        } else if (another instanceof ConnectException) {
            mStatusCode = Code.HostUnreachable;
            mUserMessage = "Service unavailable. Please, try again later";
            mExceptionMessage = another.getMessage();
        } else if (another instanceof EOFException) {
            mStatusCode = Code.UnexpectedEOF;
            mUserMessage = "Unexpected server response: empty body";
            mExceptionMessage = another.getMessage();
        } else if (another instanceof IOException || another instanceof IOError) {
            mStatusCode = Code.UnexpectedIO;
            mUserMessage = "Input/Output error: " + another.getMessage();
            mExceptionMessage = another.getMessage();
        } else {
            mStatusCode = Code.Unknown;
            mUserMessage = "DecodeError network error. Please, try again later";
            mExceptionMessage = another.getMessage();
        }
    }

    public NetworkException(int statusCode, String message) {
        mUserMessage = message;
        mStatusCode = Code.fromStatusCode(statusCode);
    }

    public NetworkException(int statusCode, String message, String cause) {
        this(statusCode, message);
        mExceptionMessage = cause;
    }

    public NetworkException(int statusCode, String message, String cause, String url) {
        this(statusCode, message, cause);
        mUrl = url;
    }

    public static boolean isNetworkError(Throwable another) {
        return another instanceof NetworkException || friendExceptions.contains(
                another.getClass());

    }

    public static Throwable convertIfNetworking(Throwable another) {
        if (another instanceof NetworkException) {
            return another;
        }

        return friendExceptions.contains(another.getClass()) ? new NetworkException(
                another) : another;
    }

    public int getStatusCode() {
        return mStatusCode.code;
    }

    public Code getCode() {
        return mStatusCode;
    }

    public String getStatusString() {
        return getCode().toString();
    }

    public boolean isTransportError() {
        return mStatusCode.code >= Code.Unknown.code;
    }

    @Nullable
    public Response<?> getResponse() {
        return mResponse;
    }

    public String getUrl() {
        return firstNonNull(mUrl, "<unknown::host>");
    }

    public String getMessage() {
        return mExceptionMessage;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public String getUserMessage() {
        return mUserMessage;
    }

    public String getExceptionMessage() {
        return mExceptionMessage;
    }
}
