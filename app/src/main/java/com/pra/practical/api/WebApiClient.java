package com.pra.practical.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * this class is used for Retrofit api client
 */
public class WebApiClient {

    private WebApi webApi;
    public static WebApiClient webApiClient;
    private static Context mcontext;

    private WebApiClient() {
        //just an empty constructor for now
    }

    public static WebApiClient getInstance(Context context) {
        if (webApiClient == null)
            webApiClient = new WebApiClient();
        mcontext = context;
        return webApiClient;
    }

    /**
     * this method is used for without access token calling api
     *
     * @return
     */
    public WebApi getWebApi() {
        try {
            System.setProperty("http.keepAlive", "false");
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);   // for display request and response parameter
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
            client.connectTimeoutMillis();

            Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(WebApi.BASE_URL + WebApi.API_PATH_POST).
                    addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            webApi = retrofit.create(WebApi.class);
            return webApi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
