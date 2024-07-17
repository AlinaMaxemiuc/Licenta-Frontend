package com.example.renting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DroneApi {
    @GET("integration-api/app/drone")
    Call<List<DroneData>> getAllDrones();
    @GET("integration-api/app/drone")
    Call<DroneResponse> getAllRentalDrones();
    @GET("integration-api/app/drone/{id}")
    Call<DroneData> getDroneById(@Path("id") String id);
    @GET("integration-api/app/drone/by-parameter")
    Call<List<DroneData>> getDroneByParameter(@Query("parameter") String parameter);
    @POST("integration-api/app/rental/check-availability")
    Call<AvailabilityResultDto> checkAvailability(@Body CheckAvailabilityDto checkAvailabilityDto);

}