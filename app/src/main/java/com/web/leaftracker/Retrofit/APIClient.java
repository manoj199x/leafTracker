package com.web.leaftracker.Retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //public static final String BASE_URL = "http://192.168.0.169/veg-app/api/"; //testing
    public static final String BASE_URL = "http://165.22.209.3/tea_weighment/public/api/"; //production
    public static APIClient mInstance;
    private static Retrofit retrofit = null;

    private APIClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static synchronized APIClient getInstance() {
        if (mInstance == null) {
            mInstance = new APIClient();
        }
        return mInstance;
    }

    public APIInterface getApiInterface() {
        return retrofit.create(APIInterface.class);
    }
}
