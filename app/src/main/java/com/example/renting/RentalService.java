package com.example.renting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RentalService {

    @POST("integration-api/app/rental")
    Call<RentalData> createRental(@Body CreateOrUpdateRentalDto rentalDto);
    @GET("integration-api/app/rental/rentals-by-customer-id/{customerId}")
    Call<RentalResponse> getRentalsByUserId(@Path("customerId") String customerId);
    @GET("integration-api/app/drone")
    Call<DroneResponse> getDrones();
}
