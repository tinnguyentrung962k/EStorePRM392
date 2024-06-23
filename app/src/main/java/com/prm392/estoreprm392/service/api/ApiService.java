package com.prm392.estoreprm392.service.api;
import com.prm392.estoreprm392.service.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
public class ApiService {
    @GET("product")
    public Call<List<Product>> getProducts() {
        return null;
    }
}
