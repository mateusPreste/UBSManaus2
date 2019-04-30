package example.android.ubsmanaus.Util;

import java.util.List;

import example.android.ubsmanaus.Model.Ubs;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class HttpRetro {
    private static final String BASE_URL = "https://api.myjson.com/";

    //Inicializa Retrofit
    public static UbsInterface getUbsClient() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return restAdapter.create(UbsInterface.class);
    }

    // Interface com métodos de requisicao
    public interface UbsInterface {
        @GET("bins/13hcng")
        Call<List<Ubs>> getUbs();
    }
}