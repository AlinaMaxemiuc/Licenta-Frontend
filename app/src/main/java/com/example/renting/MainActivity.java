package com.example.renting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    DroneApi droneApi;
    private RecyclerView recyclerView;
    private DroneAdapter adapter;
    private List<DroneData> droneList;
    private SearchView searchView;
    private View cardContainer;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        cardContainer = findViewById(R.id.cardContainer);

        droneList = new ArrayList<>();
        adapter = new DroneAdapter(this, droneList, true, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        droneApi = RetrofitClient.getClient().create(DroneApi.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            } else if (id == R.id.contact) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "alina.maxemiuc@student.usv.ro", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact from App User");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } else if (id == R.id.about) {
                FragmentManager fm = getSupportFragmentManager();
                ProfileActivity profileDialogFragment = new ProfileActivity();
                profileDialogFragment.show(fm, "fragment_profile_dialog");
            } else if (id == R.id.rentals) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", null);
                if (userId != null) {
                    Intent intent = new Intent(MainActivity.this, MyRentalsActivity.class);
                    intent.putExtra("user_id", userId); // Pass the user ID to MyRentalsActivity
                    startActivity(intent);
                } else {
                    showCustomToast("User not logged in", 1000);
                }
            } else if (id == R.id.share) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app!");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } else if (id == R.id.rate) {
                FragmentManager fm = getSupportFragmentManager();
                RateActivity rateDialogFragment = new RateActivity();
                rateDialogFragment.show(fm, "fragment_rate_dialog");
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        initDroneCards();
    }

    private void initDroneCards() {
        CardView droneOneCard = findViewById(R.id.droneone);
        CardView droneTwoCard = findViewById(R.id.dronetwo);
        CardView droneThreeCard = findViewById(R.id.dronethree);
        CardView droneFourCard = findViewById(R.id.dronefour);
        CardView droneFiveCard = findViewById(R.id.dronefive);
        CardView droneSixCard = findViewById(R.id.dronesix);

        droneOneCard.setOnClickListener(v -> fetchAndShowDroneDetails("98031C55-F7D3-4DF9-8068-3A130E979846", R.drawable.djiminitwo));
        droneTwoCard.setOnClickListener(v -> fetchAndShowDroneDetails("B6118A69-0722-938A-8635-3A1333FC1979", R.drawable.djitello));
        droneThreeCard.setOnClickListener(v -> fetchAndShowDroneDetails("68304F1E-DAD0-867D-F4AB-3A133998A498", R.drawable.djiagras));
        droneFourCard.setOnClickListener(v -> fetchAndShowDroneDetails("2B47B9FD-4BA3-6DB3-759E-3A13399C893A", R.drawable.foldablepro));
        droneFiveCard.setOnClickListener(v -> fetchAndShowDroneDetails("272D0DC8-4CCD-BE90-FCC9-3A1339A05372", R.drawable.maxavoidance));
        droneSixCard.setOnClickListener(v -> fetchAndShowDroneDetails("AC5F65E3-6D11-1C90-6DCB-3A1339A24BF9", R.drawable.zeevos));
    }

    private void performSearch(String searchText) {
        searchText = searchText.trim();
        Log.d("MainActivity", "Performing search for: " + searchText);
        Call<List<DroneData>> call = droneApi.getDroneByParameter(searchText);
        call.enqueue(new Callback<List<DroneData>>() {
            @Override
            public void onResponse(Call<List<DroneData>> call, Response<List<DroneData>> response) {
                Log.d("MainActivity", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<DroneData> searchResults = response.body();
                    for (DroneData drone : searchResults) {
                        Log.d("MainActivity", "Drone ID from API: " + drone.getId());
                    }
                    Log.d("MainActivity", "Search results: " + searchResults.toString());

                    cardContainer.setVisibility(View.GONE);

                    adapter = new DroneAdapter(MainActivity.this, searchResults, true, getSupportFragmentManager());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    Log.e("MainActivity", "Search failed, response code: " + response.code());
                    Log.e("MainActivity", "Response message: " + response.message());
                    showCustomToast("We don't have such drones", 1000);
                }
            }

            @Override
            public void onFailure(Call<List<DroneData>> call, Throwable t) {
                Log.e("MainActivity", "Network error: " + t.getMessage(), t);
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void filterList(String text) {
        List<DroneData> filteredList = new ArrayList<>();
        for (DroneData item : droneList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getModel().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter = new DroneAdapter(MainActivity.this, filteredList, true, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        if (filteredList.isEmpty()) {
            cardContainer.setVisibility(View.VISIBLE);
        } else {
            cardContainer.setVisibility(View.GONE);
        }
    }

    private void fetchAndShowDroneDetails(String droneId, int droneImageResId) {
        Call<DroneData> call = droneApi.getDroneById(droneId);
        call.enqueue(new Callback<DroneData>() {
            @Override
            public void onResponse(Call<DroneData> call, Response<DroneData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showDroneDetailsDialog(response.body(), droneImageResId);
                } else {
                    showCustomToast("Nu s-au putut obține detalii despre dronă", 1000);
                }
            }

            @Override
            public void onFailure(Call<DroneData> call, Throwable t) {
                showCustomToast("Eroare de rețea: " + t.getMessage(), 1000);
            }
        });
    }

    private void showDroneDetailsDialog(DroneData drone, int droneImageResId) {
        DroneDetailsDialogFragment dialogFragment = DroneDetailsDialogFragment.newInstance(drone, droneImageResId);
        dialogFragment.show(getSupportFragmentManager(), "dialog_drone_details");
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();

        new android.os.Handler().postDelayed(toast::cancel, duration);
    }
}
