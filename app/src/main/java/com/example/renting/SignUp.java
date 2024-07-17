package com.example.renting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private Button btnSignUp;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextPhoneNumber;
    private ApiInterface apiInterface;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private String firstName, lastName, email, password, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        btnSignUp = findViewById(R.id.btnSignUp);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        passwordToggle = findViewById(R.id.passwordToggle);
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24);
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24);
                }
                editTextPassword.setSelection(editTextPassword.length());
                isPasswordVisible = !isPasswordVisible;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageView googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = editTextFirstName.getText().toString().trim();
                lastName = editTextLastName.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String appName = "Renting";
                String userName = (firstName + lastName).trim();

                if (firstName.isEmpty()) {
                    showCustomToast("Incomplete first name.", 1000);
                    return;
                }

                if (lastName.isEmpty()) {
                    showCustomToast("Incomplete last name.", 1000);
                    return;
                }

                if (email.isEmpty()) {
                    showCustomToast("Email is required.", 1000);
                    return;
                }

                if (!isValidEmail(email)) {
                    showCustomToast("Invalid email address.", 1000);
                    return;
                }

                if (password.isEmpty()) {
                    showCustomToast("Password is required.", 1000);
                    return;
                }

                if (!isValidPassword(password)) {
                    showCustomToast("Password must contain at least one non-alphanumeric character and one uppercase letter.", 2000);
                    return;
                }

                if (phoneNumber.isEmpty()) {
                    showCustomToast("Phone number is required.", 1000);
                    return;
                }

                if (phoneNumber.length() != 10) {
                    showCustomToast("Phone number must be exactly 10 digits.", 1000);
                    return;
                }

                if (!isValidName(firstName) || !isValidName(lastName)) {
                    showCustomToast("Name and surname must contain only letters.", 1000);
                    return;
                }

                performSignUp(userName, email, password, appName, phoneNumber);
            }
        });

    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasNonAlphaNumeric = password.matches(".*[^a-zA-Z0-9].*");
        return hasUppercase && hasNonAlphaNumeric;
    }

    private void performSignUp(String userName, String email, String password, String appName, String phoneNumber) {
        Log.d(TAG, "Attempting to sign up with email: " + email);

        RegisterRequest registerRequest = new RegisterRequest(userName, email, password, appName, phoneNumber);
        Call<ResponseBody> call = apiInterface.register(registerRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User registration successful");
                    CustomerData customerData = new CustomerData();
                    customerData.setFirstName(firstName);
                    customerData.setLastName(lastName);
                    customerData.setPhoneNumber(phoneNumber);
                    customerData.setEmail(email);
                    createCustomer(customerData);
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e(TAG, "User registration failed: " + errorMessage);
                        showCustomToast("Registration failed: " + errorMessage, Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                        showCustomToast("Registration failed: " + e.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
                showCustomToast("Registration failed: " + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    private void createCustomer(CustomerData customerData) {
        Call<CustomerData> call = apiInterface.createCustomer(customerData);
        call.enqueue(new Callback<CustomerData>() {
            @Override
            public void onResponse(Call<CustomerData> call, Response<CustomerData> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Customer creation successful");

                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", response.body().getId());
                    editor.putString("user_email", customerData.getEmail());
                    editor.putString("user_first_name", customerData.getFirstName());
                    editor.putString("user_last_name", customerData.getLastName());
                    editor.putString("user_phone", customerData.getPhoneNumber());
                    editor.apply();

                    showCustomToast("Registration and customer creation successful", 1000);
                    Intent intent = new Intent(SignUp.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e(TAG, "Customer creation failed: " + errorMessage);
                        showCustomToast("Customer creation failed: " + errorMessage, Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                        showCustomToast("Customer creation failed: " + e.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerData> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
                showCustomToast("Customer creation failed: " + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    private void saveUserDetails(String email, String firstName, String lastName) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_first_name", firstName);
        editor.putString("user_last_name", lastName);
        editor.apply();
    }

    private String getUserDetail(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void handleSignInResult(GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String displayName = account.getDisplayName();
            String[] nameParts = displayName.split(" ");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";

            editTextEmail.setText(email);
            editTextFirstName.setText(firstName);
            editTextLastName.setText(lastName);

        }
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT);
        toast.show();

        new Handler().postDelayed(toast::cancel, duration);
    }
}
