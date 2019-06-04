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

package network.minter.core.internal.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;

import network.minter.core.internal.common.Acceptor;
import network.minter.core.internal.common.Lazy;
import network.minter.core.internal.common.Pair;
import network.minter.core.internal.exceptions.NetworkException;
import network.minter.core.internal.log.Mint;
import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * minter-android-core. 2018
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

	public static class Builder implements Cloneable {
		private String mBaseUrl;
		private String mDateFormat = "yyyy-MM-dd";
		private boolean mDateAsLong = true;
		private boolean mAuthRequired = false;
		private boolean mDebug = false;
		private int mConnectTimeout = 30;
		private int mReadTimeout = 30;
		private String mAuthHeaderName = "Authorization";
		private GsonBuilder mGsonBuilder;
		private Cache mHttpCache = null;
		private OnErrorListener mErrorListener;
		private HttpLoggingInterceptor.Level mDebugLevel = HttpLoggingInterceptor.Level.BODY;
		private Lazy<String> mTokenProvider;
		private Acceptor<OkHttpClient.Builder> mHttpClientConfig;
		private Acceptor<Retrofit.Builder> mRetrofitClientConfig;
		private EmptyAuthHeaderTokenListener mEmptyAuthHeaderTokenListener;
		private ArrayList<ServiceTypeAdapter> mCustomAdapters;
		private ArrayList<TypeAdapterFactory> mFactories;
		private List<Pair<String, String>> mHeaders;
		private List<Interceptor> mInterceptors;

		public Builder(String baseUrl, GsonBuilder gsonBuilder) {
			mBaseUrl = baseUrl;
			mGsonBuilder = gsonBuilder;
		}

		public Builder(String baseUrl) {
			mBaseUrl = baseUrl;
			mGsonBuilder = new GsonBuilder();
		}

		/**
		 * Configure OkHttp client before it will be created using callback
		 *
		 * @param acceptor
		 * @return
		 */
		public Builder setHttpClientConfig(Acceptor<OkHttpClient.Builder> acceptor) {
			mHttpClientConfig = acceptor;
			return this;
		}

		/**
		 * Configure retrofit before it will be created using callback
		 *
		 * @param acceptor
		 * @return
		 */
		public Builder setRetrofitClientConfig(Acceptor<Retrofit.Builder> acceptor) {
			mRetrofitClientConfig = acceptor;
			return this;
		}

		public Builder addHttpInterceptor(Interceptor interceptor) {
			if (mInterceptors == null) {
				mInterceptors = new ArrayList<>(2);
			}

			mInterceptors.add(interceptor);
			return this;
		}

		public Builder removeHttpInterceptor(Interceptor interceptor) {
			if (mInterceptors == null || interceptor == null) {
				return this;
			}

			mInterceptors.remove(interceptor);
			return this;
		}

		@Override
		public Builder clone() {
			try {
				return (Builder) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}

		public Builder setOnErrorListener(OnErrorListener errorListener) {
			mErrorListener = errorListener;
			return this;
		}

		public Builder registerTypeAdapter(ServiceTypeAdapter adapter) {
			if (mCustomAdapters == null) {
				mCustomAdapters = new ArrayList<>(2);
			}
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
			if (mHeaders == null) {
				mHeaders = new ArrayList<>(2);
			}
			mHeaders.add(new Pair<>(key, value));
			return this;
		}

		public ApiService.Builder setDebug(boolean debug) {
			mDebug = debug;
			return this;
		}

		public Builder setTokenGetter(Lazy<String> callback) {
			mTokenProvider = callback;
			return this;
		}

		public Builder registerTypeAdapter(Type type, Object object) {
			return registerTypeAdapter(new ServiceTypeAdapter(type, object));
		}

		public Builder registerTypeAdapterFactory(TypeAdapterFactory adapterFactory) {
			if (mFactories == null) {
				mFactories = new ArrayList<>(2);
			}
			mFactories.add(adapterFactory);
			return this;
		}

		public Builder setDateFormat(String dateFormat) {
			mDateFormat = dateFormat;
			mDateAsLong = false;
			return this;
		}

		public Builder setDateAsLong(boolean b) {
			mDateAsLong = b;
			return this;
		}

		/**
		 * Pass to any request doing with current client instance, http header with auth token
		 *
		 * @param required
		 * @return
		 */
		public Builder authRequired(boolean required) {
			mAuthRequired = required;
			return this;
		}

		public Builder authRequired() {
			mAuthRequired = true;
			return this;
		}

		public Builder setCache(Cache httpCache) {
			mHttpCache = httpCache;
			return this;
		}

		public ApiService.Builder setEmptyAuthTokenListener(EmptyAuthHeaderTokenListener listener) {
			mEmptyAuthHeaderTokenListener = listener;
			return this;
		}

		public Builder setDebugRequestLevel(HttpLoggingInterceptor.Level level) {
			mDebugLevel = level;
			return this;
		}

		public Retrofit build() {
			Gson gson = buildGSON();
			OkHttpClient client = buildHttpClient();

			final Retrofit.Builder builder = new Retrofit.Builder()
					.addConverterFactory(GsonConverterFactory.create(gson));

			if (mRetrofitClientConfig != null) {
				mRetrofitClientConfig.accept(builder);
			}

			return builder.baseUrl(mBaseUrl)
					.client(client)
					.build();
		}

		private Gson buildGSON() {
			mGsonBuilder
					.serializeNulls()
					.setDateFormat(mDateFormat);

			if (mCustomAdapters != null) {
				for (ServiceTypeAdapter adapter : mCustomAdapters) {
					mGsonBuilder.registerTypeAdapter(adapter.type, adapter.object);
				}
			}

			if (mFactories != null) {
				for (TypeAdapterFactory factory : mFactories) {
					mGsonBuilder.registerTypeAdapterFactory(factory);
				}
			}

			if (mDateAsLong) {
				mGsonBuilder.registerTypeAdapter(Date.class,
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

				if (mHeaders != null) {
					for (final Pair<String, String> kv : mHeaders) {
						request.addHeader(kv.first, kv.second);
					}
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

			if (mHttpCache != null) {
				httpClient.cache(mHttpCache);
			}

			if (mDebug) {
				HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Mint.tag("OkHttp").d(message));
				loggingInterceptor.setLevel(mDebugLevel);
				httpClient.addInterceptor(loggingInterceptor);
			}
			httpClient.addInterceptor(buildAuthInterceptor());

			httpClient.connectTimeout(mConnectTimeout, TimeUnit.SECONDS);
			httpClient.readTimeout(mReadTimeout, TimeUnit.SECONDS);

			if (mDebug && mDebugLevel == HttpLoggingInterceptor.Level.BODY) {
				// request headers does not logging
				httpClient.addInterceptor(chain -> {
					for (Map.Entry<String, List<String>> item : chain.request().headers().toMultimap().entrySet()) {
						for (String sub : item.getValue()) {
							Mint.tag("OkHttp").d("%s: %s", item.getKey(), sub);
						}
					}
					return chain.proceed(chain.request());
				});
			}

			if (mHttpClientConfig != null) {
				mHttpClientConfig.accept(httpClient);
			}

			if (mInterceptors != null) {
				for (Interceptor i : mInterceptors) {
					httpClient.addInterceptor(i);
				}
			}

			return enableTls12OnPreLollipop(httpClient).build();
		}

		@SuppressWarnings("deprecation")
		private OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
            try {
                Class tmp = Class.forName("android.os.Build");

                if (android.os.Build.VERSION.SDK_INT >= 18 && android.os.Build.VERSION.SDK_INT < 22) {
                    try {
                        SSLContext sc = SSLContext.getInstance("TLSv1.2");
                        sc.init(null, null, null);
                        client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                        ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                                .tlsVersions(TlsVersion.TLS_1_2)
                                .build();

                        List<ConnectionSpec> specs = new ArrayList<>();
                        specs.add(cs);
                        specs.add(ConnectionSpec.COMPATIBLE_TLS);
                        specs.add(ConnectionSpec.CLEARTEXT);

                        client.connectionSpecs(specs);
                    } catch (Exception exc) {
                        Timber.e(exc);
                    }
                }
            } catch (ClassNotFoundException ignore) {
            }

			return client;
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
}
