package com.example.renting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRentalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RentalsAdapter adapter;
    private List<RentalData> rentalList;
    private Map<String, DroneData> droneDataMap;
    private RentalService rentalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rentals);

        recyclerView = findViewById(R.id.recyclerView);
        rentalList = new ArrayList<>();
        droneDataMap = new HashMap<>();
        adapter = new RentalsAdapter(this, rentalList, droneDataMap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = RetrofitClient.getClient();
        rentalService = retrofit.create(RentalService.class);

        Intent intent = getIntent();
        String customerId = intent.getStringExtra("user_id");

        if (customerId != null) {
            fetchRentalsByUserId(customerId);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchRentalsByUserId(String customerId) {
        Call<RentalResponse> call = rentalService.getRentalsByUserId(customerId);
        call.enqueue(new Callback<RentalResponse>() {
            @Override
            public void onResponse(@NonNull Call<RentalResponse> call, @NonNull Response<RentalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rentalList.clear();
                    rentalList.addAll(response.body().getItems());
                    fetchDroneData();
                } else {
                    Toast.makeText(MyRentalsActivity.this, "Failed to fetch rentals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RentalResponse> call, @NonNull Throwable t) {
                Toast.makeText(MyRentalsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyRentalsActivity", "Error fetching rentals", t);
            }
        });
    }
    private void fetchDroneData() {
        Call<DroneResponse> call = rentalService.getDrones();
        call.enqueue(new Callback<DroneResponse>() {
            @Override
            public void onResponse(@NonNull Call<DroneResponse> call, @NonNull Response<DroneResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    droneDataMap.clear();
                    for (DroneData droneData : response.body().getItems()) {
                        droneDataMap.put(droneData.getId(), droneData);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyRentalsActivity.this, "Failed to fetch drone data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DroneResponse> call, @NonNull Throwable t) {
                Toast.makeText(MyRentalsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyRentalsActivity", "Error fetching drone data", t);
            }
        });
    }
}
