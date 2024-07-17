package com.example.renting;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerApi {
    @GET("integration-api/app/customer")
    Call<CustomerData> getCustomer();
    @PUT("integration-api/app/customer/{id}")
    Call<Void> updateCustomer(@Path("id") String customerId, @Body CustomerData customer);
}
