package network.minter.bipwallet.internal;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import network.minter.bipwallet.internal.exceptions.ResponseException;
import network.minter.mintercore.internal.exceptions.NetworkException;
import network.minter.my.MyMinterApi;
import network.minter.my.models.MyResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ReactiveAdapter {
    public static <T> Observable<T> rxCall(Call<T> call) {
        return Observable.create(emitter -> call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call1, @NonNull Response<T> response) {
                if(response.body() == null) {
                    emitter.onNext((T)createErrorResult(response));
                } else {
                    emitter.onNext(response.body());
                }

                emitter.onComplete();
            }

            @Override
            public void onFailure(@NonNull Call<T> call1, @NonNull Throwable t) {
                emitter.onError(t);
            }
        }));
    }

    public static <T> Function<? super Throwable, ? extends ObservableSource<? extends MyResult<T>>> convertToErrorResult() {
        return (Function<Throwable, ObservableSource<? extends MyResult<T>>>) throwable
                -> {
            if (!(throwable instanceof HttpException)) {
                return Observable.error(throwable);
            }

            return Observable.just(createErrorResult(((HttpException) throwable)));
        };
    }

    public static <T> MyResult<T> createErrorResult(final String json) {
        Gson gson = MyMinterApi.getInstance().getGsonBuilder().create();
        return gson.fromJson(json, new TypeToken<MyResult<T>>() {
        }.getType());
    }

    public static <T> MyResult<T> createErrorResult(final Response<T> response) {
        final String errorBodyString;
        try {
            // нельзя после этой строки пытаться вытащить body из ошибки,
            // потому что retrofit по какой-то причине не хранит у себя это значение
            // а держит в буффере до момента первого доступа
            errorBodyString = response.errorBody().string();
        } catch (IOException e) {
            Timber.e(e, "Unable to resolve http exception response");
            return createEmpty();
        }

        return createErrorResult(errorBodyString);
    }

    public static <T> MyResult<T> createErrorResult(final HttpException exception) {
        final String errorBodyString;
        try {
            // нельзя после этой строки пытаться вытащить body из ошибки,
            // потому что retrofit по какой-то причине не хранит у себя это значение
            // а держит в буффере до момента первого доступа
            errorBodyString = ((HttpException) exception).response().errorBody().string();
        } catch (IOException e) {
            Timber.e(e, "Unable to resolve http exception response");
            return createEmpty();
        }

        return createErrorResult(errorBodyString);
    }

    public static <T> MyResult<T> createEmpty() {
        return new MyResult<>();
    }
}
