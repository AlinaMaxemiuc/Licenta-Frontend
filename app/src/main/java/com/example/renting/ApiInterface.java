package com.example.renting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("/api/account/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);
    @POST("api/account/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/integration-api/app/customer")
    Call<CustomerData> createCustomer(@Body CustomerData customerData);
    @GET("/integration-api/app/customer")
    Call<ResponseBody> getCustomerByEmail(@Query("email") String email);
    @GET("api/identity/users/by-email/{email}")
    Call<UserResponse> getUserByEmail(@Path("email") String email);
    @GET("identity/users")
    Call<UserResponse> getUsers();
    @POST("identity/users/reset_password")
    Call<ResponseBody> resetPassword(@Body PasswordResetRequest passwordResetRequest);
    @PUT("api/identity/users/{id}/password")
    Call<ResponseBody> updatePassword(@Path("id") String id, @Body PasswordResetRequest passwordResetRequest);
    @POST("api/account/check-password")
    Call<PasswordCheckResponse> checkPassword(@Body PasswordCheckRequest passwordCheckRequest);
}
