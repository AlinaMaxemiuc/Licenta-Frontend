package com.example.renting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText firstName, lastName, phoneNumber, email;
    private ImageView profileImage;
    private Uri imageUri;
    private CustomerApi customerApi;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        email = view.findViewById(R.id.email);
        profileImage = view.findViewById(R.id.profileImage);
        Button saveButton = view.findViewById(R.id.saveButton);

        profileImage.setOnClickListener(v -> openImageChooser());

        saveButton.setOnClickListener(v -> saveProfile());

        customerApi = RetrofitClient.getClient().create(CustomerApi.class);
        sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loadProfile();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProfile() {
        String firstNameValue = sharedPreferences.getString("user_first_name", "");
        String lastNameValue = sharedPreferences.getString("user_last_name", "");
        String phoneValue = sharedPreferences.getString("user_phone", "");
        String emailValue = sharedPreferences.getString("user_email", "");

        firstName.setText(firstNameValue);
        lastName.setText(lastNameValue);
        phoneNumber.setText(phoneValue);
        email.setText(emailValue);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10 && phoneNumber.matches("\\d{10}");
    }

    private void saveProfile() {
        String userId = sharedPreferences.getString("user_id", null);
        if (userId == null) return;

        String updatedFirstName = firstName.getText().toString();
        String updatedLastName = lastName.getText().toString();
        String updatedPhoneNumber = phoneNumber.getText().toString();
        String updatedEmail = email.getText().toString();

        if (!isValidEmail(updatedEmail)) {
            showCustomToast("Invalid email address.", 1000);
            return;
        }

        if (!isValidPhoneNumber(updatedPhoneNumber)) {
            showCustomToast("Phone number must be 10 digits.", 1000);
            return;
        }

        CustomerData updatedCustomer = new CustomerData();
        updatedCustomer.setId(userId);
        updatedCustomer.setFirstName(updatedFirstName);
        updatedCustomer.setLastName(updatedLastName);
        updatedCustomer.setPhoneNumber(updatedPhoneNumber);
        updatedCustomer.setEmail(updatedEmail);

        Call<Void> call = customerApi.updateCustomer(userId, updatedCustomer);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_first_name", updatedFirstName);
                    editor.putString("user_last_name", updatedLastName);
                    editor.putString("user_phone", updatedPhoneNumber);
                    editor.putString("user_email", updatedEmail);
                    editor.apply();

                    showCustomToast("Profile updated successfully", 1000);
                    dismiss();
                } else {
                    showCustomToast("Failed to update profile", 1000);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();

        new Handler(Looper.getMainLooper()).postDelayed(toast::cancel, duration);
    }
}
