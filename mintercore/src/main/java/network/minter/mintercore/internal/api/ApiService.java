package network.minter.mintercore.internal.api;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import network.minter.mintercore.internal.common.CallbackProvider;
import network.minter.mintercore.internal.exceptions.NetworkException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class ApiService {
    public interface EmptyAuthHeaderTokenListener {
        void doOnEmptyAuthHeader();
    }

    public interface OnErrorListener {
        void onError(int httpCode, Response response);
    }

    public static class Builder {
        private String mDateFormat = "yyyy-MM-dd";
        private boolean mDateAsLong = true;
        private ArrayList<ServiceTypeAdapter> mCustomAdapters = new ArrayList<>();
        private ArrayList<TypeAdapterFactory> mFactories = new ArrayList<>();
        private boolean mAuthRequired = false;
        private OnErrorListener mErrorListener;

        private HashMap<String, String> mHeaders = new HashMap<>();
        private String mBaseUrl;
        private EmptyAuthHeaderTokenListener mEmptyAuthHeaderTokenListener;
        private CallbackProvider<String> mTokenProvider;
        private boolean mDebug = false;
        private int mConnectTimeout = 30;
        private int mReadTimeout = 30;
        private String mAuthHeaderName = "Authorization";
        private HttpLoggingInterceptor.Level mDebugLevel = HttpLoggingInterceptor.Level.BODY;
        private GsonBuilder mGsonBuilder;

        public Builder(String baseUrl, GsonBuilder gsonBuilder) {
            mBaseUrl = baseUrl;
            mGsonBuilder = gsonBuilder;
        }

        public Builder(String baseUrl) {
            mBaseUrl = baseUrl;
            mGsonBuilder = new GsonBuilder();
        }

        public Builder setOnErrorListener(OnErrorListener errorListener) {
            mErrorListener = errorListener;
            return this;
        }

        public Builder registerTypeAdapter(ServiceTypeAdapter adapter) {
            mCustomAdapters.add(adapter);
            return this;
        }

        public Builder setAuthHeaderName(String authHeaderName) {
            mAuthHeaderName = authHeaderName;
            return this;
        }

        public Builder setConnectionTimeout(int seconds) {
            mConnectTimeout = seconds;
            return this;
        }

        public Builder setReadTimeout(int seconds) {
            mReadTimeout = seconds;
            return this;
        }

        public Builder addHeader(String key, String value) {
            mHeaders.put(key, value);
            return this;
        }

        public ApiService.Builder setDebug(boolean debug) {
            mDebug = debug;
            return this;
        }

        public Builder setTokenGetter(CallbackProvider<String> callback) {
            mTokenProvider = callback;
            return this;
        }

        public Builder registerTypeAdapter(Type type, Object object) {
            mCustomAdapters.add(new ServiceTypeAdapter(type, object));
            return this;
        }

        public Builder registerTypeAdapterFactory(TypeAdapterFactory adapterFactory) {
            mFactories.add(adapterFactory);
            return this;
        }

        public Builder setDateFormat(String dateFormat) {
            this.mDateFormat = dateFormat;
            this.mDateAsLong = false;
            return this;
        }

        public Builder setDateAsLong(boolean b) {
            this.mDateAsLong = b;
            return this;
        }

        public Builder authRequired(boolean required) {
            mAuthRequired = required;
            return this;
        }

        public Builder authRequired() {
            mAuthRequired = true;
            return this;
        }

        public Retrofit build() {
            Gson gson = buildGSON();
            OkHttpClient client = buildHttpClient();

            return new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        public ApiService.Builder setEmptyAuthTokenListener(EmptyAuthHeaderTokenListener listener) {
            mEmptyAuthHeaderTokenListener = listener;
            return this;
        }

        public Builder setDebugRequestLevel(HttpLoggingInterceptor.Level level) {
            mDebugLevel = level;
            return this;
        }

        private Gson buildGSON() {
            mGsonBuilder
                    .serializeNulls()
                    .setDateFormat(mDateFormat);

            for (ServiceTypeAdapter adapter : mCustomAdapters) {
                mGsonBuilder.registerTypeAdapter(adapter.type, adapter.object);
            }

            for (TypeAdapterFactory factory : mFactories) {
                mGsonBuilder.registerTypeAdapterFactory(factory);
            }

            if (mDateAsLong) {
                mGsonBuilder = mGsonBuilder.registerTypeAdapter(Date.class,
                                                                (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(
                                                                        json.getAsJsonPrimitive().getAsLong() * 1000));
            }

            return mGsonBuilder.create();
        }

        @Nullable
        private String getToken() {
            return mTokenProvider == null ? null : mTokenProvider.get();
        }

        private Interceptor buildAuthInterceptor() {
            return chain -> {
                Request original = chain.request();

                Request.Builder request = original.newBuilder();
                for (Map.Entry<String, String> kv : mHeaders.entrySet()) {
                    request.addHeader(kv.getKey(), kv.getValue());
                }

                if (!mAuthRequired) {
                    Request built = request
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(built);
                }

                if (getToken() == null || getToken().isEmpty()) {
                    if (mEmptyAuthHeaderTokenListener != null) {
                        mEmptyAuthHeaderTokenListener.doOnEmptyAuthHeader();
                    }
                    throw new NetworkException(401, "Authorization required",
                                               "HTTP auth header is empty or null");
                }

                request.addHeader(mAuthHeaderName, getToken());
                request.cacheControl(new CacheControl.Builder()
                                             .noCache()
                                             .noStore()
                                             .maxAge(0, TimeUnit.SECONDS)
                                             .build()
                );

                Request built = request.build();

                Response response = chain.proceed(built);
                if (response.code() >= 400 && mErrorListener != null) {
                    mErrorListener.onError(response.code(), response);
                }
                return response;
            };
        }

        private OkHttpClient buildHttpClient() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if (mDebug) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(mDebugLevel);
                httpClient.addInterceptor(loggingInterceptor);
            }
            httpClient.addInterceptor(buildAuthInterceptor());

            httpClient.connectTimeout(mConnectTimeout, TimeUnit.SECONDS);
            httpClient.readTimeout(mReadTimeout, TimeUnit.SECONDS);

            if (mDebug) {
//                httpClient.addInterceptor(chain -> {
//                    Stream.of(chain.request().headers().toMultimap().entrySet())
//                            .forEach(item -> {
//                                Stream.of(item.getValueBigInteger()).forEach(sub -> {
//                                    Timber.tag("OkHttp").d("%s: %s", item.getKey(), sub);
//                                });
//                            });
//                    return chain.proceed(chain.request());
//                });
            }


            return httpClient.build();
        }
    }

    public static class ServiceTypeAdapter {
        public Type type;
        public Object object;

        public ServiceTypeAdapter(Type type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public static class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<? super T> rawType = type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public void write(JsonWriter writer, String value)
                throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }

        public String read(JsonReader reader)
                throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }
    }
}
