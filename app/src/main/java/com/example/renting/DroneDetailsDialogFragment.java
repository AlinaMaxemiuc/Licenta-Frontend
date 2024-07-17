package com.example.renting;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DroneDetailsDialogFragment extends DialogFragment {

    private static final String ARG_DRONE = "drone";
    private static final String ARG_DRONE_IMAGE_RES = "drone_image_res";

    private DroneData drone;
    private int droneImageResId;

    public static DroneDetailsDialogFragment newInstance(DroneData drone, int droneImageResId) {
        DroneDetailsDialogFragment fragment = new DroneDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DRONE, drone);
        args.putInt(ARG_DRONE_IMAGE_RES, droneImageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drone = getArguments().getParcelable(ARG_DRONE);
            droneImageResId = getArguments().getInt(ARG_DRONE_IMAGE_RES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_drone_details, container, false);
        ImageView droneImageView = view.findViewById(R.id.drone_image);
        TextView nameTextView = view.findViewById(R.id.drone_name);
        TextView modelTextView = view.findViewById(R.id.drone_model);
        TextView utilityTextView = view.findViewById(R.id.drone_utility);
        TextView categoryTextView = view.findViewById(R.id.drone_category);
        TextView productionYearTextView = view.findViewById(R.id.drone_production_year);
        Button okButton = view.findViewById(R.id.dialogButton);
        Button rentButton = view.findViewById(R.id.rentButton);

        droneImageView.setImageResource(droneImageResId);

        if (drone != null) {
            nameTextView.setText(drone.getName());
            modelTextView.setText("Model: " + drone.getModel());
            utilityTextView.setText("Utility: " + drone.getUtilityAsString());
            categoryTextView.setText("Category: " + drone.getCategoryAsString());
            productionYearTextView.setText("Production year: " + extractYearFromDate(drone.getProductionYear()));
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rent button clicked");
                if (isUserLoggedIn()) {
                    Log.d(TAG, "User is logged in, redirecting to rent page with DRONE_ID: " + drone.getId());
                    Intent intent = new Intent(requireContext(), RentPage.class);
                    intent.putExtra("DRONE_ID", drone.getId());
                    startActivity(intent);
                } else {
                    Log.d(TAG, "User is not logged in, redirecting to login page.");
                    Intent intent = new Intent(requireContext(), LoginPage.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", null);
        boolean isLoggedIn = email != null;
        Log.d(TAG, "Checking if user is logged in: " + isLoggedIn + ", email: " + email);
        return isLoggedIn;
    }

    private String extractYearFromDate(String date) {
        if (date != null && date.length() >= 4) {
            return date.substring(0, 4);
        }
        return "";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(requireContext(), getTheme());
    }
}
