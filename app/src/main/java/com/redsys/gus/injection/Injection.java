package com.redsys.gus.injection;

import com.redsys.gus.data.UserRepository;
import com.redsys.gus.data.UserRepositoryImpl;
import com.redsys.gus.data.remote.GithubUserRestService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by a on 9/20/16.
 */

public class Injection {

    private static final String BASE_URL = "https://api.github.com";

    private static OkHttpClient okHttpClient;
    private static GithubUserRestService userRestService;
    private static Retrofit retrofitInstance;

    public static void init() {

    }

    public static UserRepository provideUserRepo() {
        return new UserRepositoryImpl(provideGithubUserRestService());
    }

    public static GithubUserRestService provideGithubUserRestService() {

        if (userRestService == null) {

            userRestService = getRetrofitInstance()
                    .create(GithubUserRestService.class);
        }
        return userRestService;
    }

    public static OkHttpClient getOkHttpClient() {

        if (okHttpClient == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }

        return okHttpClient;
    }

    public static Retrofit getRetrofitInstance() {

        if (retrofitInstance == null) {

            Retrofit.Builder retrofit = new Retrofit.Builder()
                    .client(Injection.getOkHttpClient())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

            retrofitInstance = retrofit.build();
        }
        return retrofitInstance;
    }
}
