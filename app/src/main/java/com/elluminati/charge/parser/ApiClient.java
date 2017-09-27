package com.elluminati.charge.parser;

import android.support.annotation.NonNull;

import com.elluminati.charge.utils.Const;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.elluminati.charge.utils.Const.BASE_URL;


public class ApiClient {

    public static final String Tag = "ApiClient";
    private static final int CONNECTION_TIMEOUT = 30; //seconds
    private static final int READ_TIMEOUT = 20; //seconds
    private static final int WRITE_TIMEOUT = 20; //seconds
    public static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("placeholder/*");
    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Retrofit retrofit = null;
    private static Gson gson;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout
                    (CONNECTION_TIMEOUT,
                            TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,
                            TimeUnit.SECONDS).addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // Request customization: add request headers
                            Request.Builder requestBuilder = chain.request().newBuilder()
                                    .header("x-lsq", Const.X_LSQ).header("x-lsq-host", Const
                                            .X_LSQ_HOST).header("Authorization",
                                            Const.BEARER);
                            return chain.proceed(requestBuilder.build());
                        }
                    }).build();


            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }

    @NonNull
    public static String JSONResponse(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(jsonObject);
    }
    public Retrofit changeApiBaseUrl(String newApiBaseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout
                (CONNECTION_TIMEOUT,
                        TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,
                        TimeUnit.SECONDS).build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(newApiBaseUrl).build();
    }

}