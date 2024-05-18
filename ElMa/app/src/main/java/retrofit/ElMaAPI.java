package retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ElMaAPI {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        String url;
        url = "http://194.146.242.26:7777/";
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
