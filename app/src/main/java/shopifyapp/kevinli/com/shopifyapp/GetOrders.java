package shopifyapp.kevinli.com.shopifyapp;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kevin on 5/9/2018.
 */

public interface GetOrders {
    @GET("/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
    Call<OrderList> getOrderList();
}
