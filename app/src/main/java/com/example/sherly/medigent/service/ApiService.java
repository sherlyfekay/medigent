package com.example.sherly.medigent.service;

import android.content.SharedPreferences;

import com.example.sherly.medigent.fitur.histori.HistoryActivity;
import com.example.sherly.medigent.model.DeleteModel;
import com.example.sherly.medigent.model.agent.AgentModel;
import com.example.sherly.medigent.model.alamat.AddressModel;
import com.example.sherly.medigent.model.alamat.GetAddressModel;
import com.example.sherly.medigent.model.alamat.PostAddressModel;
import com.example.sherly.medigent.model.artikel.ArticleModel;
import com.example.sherly.medigent.model.histori.DetailHistoryModel;
import com.example.sherly.medigent.model.histori.HistoryModel;
import com.example.sherly.medigent.model.login.LoginModel;
import com.example.sherly.medigent.model.login.PostLoginModel;
import com.example.sherly.medigent.model.mainmenu.DataRole;
import com.example.sherly.medigent.model.mainmenu.RoleModel;
import com.example.sherly.medigent.model.pasien.GetPatientModel;
import com.example.sherly.medigent.model.pasien.PatientModel;
import com.example.sherly.medigent.model.pasien.PostPatientModel;
import com.example.sherly.medigent.model.pemesanan.OrderModel;
import com.example.sherly.medigent.model.pemesanan.PostOrderModel;
import com.example.sherly.medigent.model.penawaran.OfferModel;
import com.example.sherly.medigent.model.penawaran.PostOfferModel;
import com.example.sherly.medigent.model.register.PostRegisterModel;
import com.example.sherly.medigent.model.register.RegisterModel;
import com.example.sherly.medigent.model.shift.ShiftModel;
import com.example.sherly.medigent.model.user.UpdateUserModel;
import com.example.sherly.medigent.model.user.UserModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static android.content.Context.MODE_PRIVATE;

public class ApiService {
    //Konek ke hotspot sherly
    //public static String BASE_URL = "http://192.168.43.157:3000";
    //Konek 3rdFloor Paguma
    public static String BASE_URL = "https://sherly.jagopesan.com";

    public static PostService service_post = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.PostService.class);

    public static GetService service_get = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.GetService.class);

    public static PatchService service_patch = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.PatchService.class);

    public static DeleteService service_delete = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService.DeleteService.class);

    public interface GetService {
        @GET("/roles")
        Call<RoleModel> getRole();

        @GET("/roles/{roleId}")
        Call<DataRole> getDetailRole(@Header("Authorization") String token, @Path("roleId") String roleId);

        @GET("/agents")
        Call<AgentModel> getAgents(@Header("Authorization") String token);

        @GET("/users/{userId}")
        Call<UserModel> getDetailUser(@Header("Authorization") String token, @Path("userId") String userId);

        @GET("/patients/category/{userId}")
        Call<GetPatientModel> getPatientByUser(@Header("Authorization") String token, @Path("userId") String userId);

        @GET("/addresses/category/{userId}")
        Call<GetAddressModel> getAddressByUser(@Header("Authorization") String token, @Path("userId") String userId);

        @GET("/ordersoffers/category/{userId}")
        Call<HistoryModel> getHistoryByUser(@Header("Authorization") String token, @Path("userId") String userId);

        @GET("/ordersoffers/{ooId}")
        Call<DetailHistoryModel> getHistoryByOO (@Header("Authorization") String token, @Path("ooId") String ooId);

        @GET("/shifts/category/{ooId}")
        Call<ShiftModel> getShiftByOO (@Header("Authorization") String token, @Path("ooId") String ooId);

        @GET("/articles")
        Call<ArticleModel> getArticle(@Header("Authorization") String token);

        /*@GET("/poli/api")
        Call<PoliModel> getPoli();

        @GET("/dokter/api/poli/{id_poli}")
        Call<DokterModel> getDokterPoli(@Path("id_poli") String id_poli);

        @GET("/dokter/api")
        Call<DokterModel> getDokter();

        @GET("/dokter/api/{id_dokter}")
        Call<DokterModel> getDetailDokter(@Path("id_dokter") String id_dokter);

        @GET("/pasien/api/{id_pasien}")
        Call<PasienModel> getPasien(@Path("id_pasien") String id_pasien);

        @GET("/dokter/api/jadwal/{id_dokter}")
        Call<JadwalModel> getJadwal(@Path("id_dokter") String id_dokter);*/
    }

    public interface PostService {
//        @FormUrlEncoded
//        @POST("/users/login")
//        Call<LoginModel>postLogin(@Field("email") String email, @Field("password") String password);
        //@Headers("Content-Type: application/json")
        @POST("/users/login")
        Call<LoginModel> postLogin(@Body PostLoginModel body);

        @POST("/users/signup")
        Call<RegisterModel> postSignup(@Body PostRegisterModel body);

        @POST("/patients")
        Call<PatientModel> postPatient(@Header("Authorization") String token, @Body PostPatientModel body);

        @POST("/ordersoffers/orders")
        Call<OrderModel> postOrder(@Header("Authorization") String token, @Body PostOrderModel body);

        @POST("/ordersoffers/offers")
        Call<OfferModel> postOffer(@Header("Authorization") String token, @Body PostOfferModel body);

        @POST("/addresses")
        Call<PostAddressModel> postAddress(@Header("Authorization") String token, @Body AddressModel body);
    }

    public interface PatchService {
        @PATCH("/users/{userId}")
        Call<UserModel> patchUser(@Header("Authorization") String token, @Path("userId") String userId, @Body UpdateUserModel body);

        @PATCH("/addresses/{addressId}")
        Call<DeleteModel> patchAddress(@Header("Authorization") String token, @Path("addressId") String userId, @Body UpdateUserModel body);
    }

    public interface DeleteService {
        @DELETE("/addresses/{addressId}")
        Call<DeleteModel> deleteAddress(@Header("Authorization") String token, @Path("addressId") String addressId);
    }
}
