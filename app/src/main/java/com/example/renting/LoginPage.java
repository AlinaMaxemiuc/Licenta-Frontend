package com.example.renting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "LoginPage";
    private ApiInterface apiInterface;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxRememberMe;
    private Button btnLogin;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        btnLogin = findViewById(R.id.btnLogin);

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

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("auth_token", null);

        String savedEmail = sharedPreferences.getString("user_email", null);
        if (savedEmail != null) {
            editTextEmail.setText(savedEmail);
        }

        if (authToken != null) {
            Retrofit retrofit = RetrofitClient.getClientToken(authToken);
            apiInterface = retrofit.create(ApiInterface.class);
        } else {
            Retrofit retrofit = RetrofitClient.getClient();
            apiInterface = retrofit.create(ApiInterface.class);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameOrEmailAddress = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                boolean rememberMe = checkBoxRememberMe.isChecked();

                if (userNameOrEmailAddress.isEmpty() || password.isEmpty()) {
                    showCustomToast("You must login first.", 1000);
                    return;
                }

                checkPassword(userNameOrEmailAddress, password, rememberMe);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    private void checkPassword(String email, String password, boolean rememberMe) {
        PasswordCheckRequest passwordCheckRequest = new PasswordCheckRequest(email, password);
        Call<PasswordCheckResponse> call = apiInterface.checkPassword(passwordCheckRequest);
        call.enqueue(new Callback<PasswordCheckResponse>() {
            @Override
            public void onResponse(Call<PasswordCheckResponse> call, Response<PasswordCheckResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PasswordCheckResponse passwordCheckResponse = response.body();
                    if (passwordCheckResponse.isValid()) {
                        login(email, password, rememberMe);  // Continuă login-ul după verificarea parolei
                    } else {
                        showCustomToast("Incorrect password.", 1000);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Password check failed: " + errorBody);
                        showCustomToast("Password check failed: " + errorBody, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                        showCustomToast("Error reading error body: " + e.getMessage(), 1000);
                    }
                }
            }

            @Override
            public void onFailure(Call<PasswordCheckResponse> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
                t.printStackTrace();
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String idToken = account.getIdToken();

            getUserDetailsForGoogle(email, idToken);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getUserDetailsForGoogle(String email, String idToken) {
        Call<ResponseBody> call = apiInterface.getCustomerByEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");

                        if (itemsArray.length() > 0) {
                            JSONObject customerObject = itemsArray.getJSONObject(0);
                            String userId = customerObject.optString("id");
                            String firstName = customerObject.optString("firstName");
                            String lastName = customerObject.optString("lastName");
                            String phoneNumber = customerObject.optString("phoneNumber");

                            saveUserDetails(email, userId, firstName, lastName, phoneNumber);
                            navigateToMainActivity();
                        } else {
                            showCustomToast("User not found.", 1000);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        showCustomToast("Error parsing user details.", 1000);
                    }
                } else {
                    showCustomToast("Failed to fetch user details", 1000);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed to fetch user details: " + t.getMessage());
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void saveUserDetails(String email, String userId, String firstName, String lastName, String phoneNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_id", userId);
        editor.putString("user_first_name", firstName);
        editor.putString("user_last_name", lastName);
        editor.putString("user_phone", phoneNumber);
        editor.apply();
    }

    private void getUserDetails(String email) {
        Call<ResponseBody> call = apiInterface.getCustomerByEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");

                        if (itemsArray.length() > 0) {
                            JSONObject customerObject = itemsArray.getJSONObject(0);
                            String userId = customerObject.optString("id");
                            String firstName = customerObject.optString("firstName");
                            String lastName = customerObject.optString("lastName");
                            String phoneNumber = customerObject.optString("phoneNumber");

                            saveUserDetails(email, userId, firstName, lastName, phoneNumber);
                            navigateToMainActivity();
                        } else {
                            showCustomToast("User not found.", 1000);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        showCustomToast("Error parsing user details.", 1000);
                    }
                } else {
                    showCustomToast("Failed to fetch user details", 1000);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed to fetch user details: " + t.getMessage());
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginWithGoogle(String email, String idToken) {
        LoginRequest loginRequest = new LoginRequest(email, idToken);
        Call<LoginResponse> call = apiInterface.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveAuthToken(token);
                } else {
                    Log.e(TAG, "Google login failed: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
            }
        });
    }

    private void login(String email, String password, boolean rememberMe) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiInterface.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveAuthToken(token); // Save the token
                    checkCustomerEmailExists(token, email); // Check customer email
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Login failed: " + errorBody);
                        showCustomToast("Login failed: " + errorBody, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                        showCustomToast("Login failed: " + e.getMessage(), 1000);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
                t.printStackTrace();
                showCustomToast("Network error: " + t.getMessage(), 1000);
            }
        });
    }

    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    private void checkCustomerEmailExists(String token, String email) {
        if (apiInterface == null) {
            Log.e(TAG, "API interface is not initialized");
            showCustomToast("API interface is not initialized", 1000);
            return;
        }

        Log.d(TAG, "Fetching customer by email: " + email);

        Call<ResponseBody> call = apiInterface.getCustomerByEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");

                        boolean emailExists = false;
                        String userId = null;
                        String firstName = null;
                        String lastName = null;
                        String phoneNumber = null;
                        String savedEmail = null;
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject customerObject = itemsArray.getJSONObject(i);
                            if (email.equals(customerObject.optString("email"))) {
                                emailExists = true;
                                userId = customerObject.optString("id");
                                firstName = customerObject.optString("firstName");
                                lastName = customerObject.optString("lastName");
                                phoneNumber = customerObject.optString("phoneNumber");
                                savedEmail = customerObject.optString("email");
                                break;
                            }
                        }

                        if (emailExists) {
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", userId);
                            editor.putString("user_first_name", firstName);
                            editor.putString("user_last_name", lastName);
                            editor.putString("user_phone", phoneNumber);
                            editor.putString("user_email", savedEmail);
                            editor.apply();

                            Log.d(TAG, "User found and details saved: " + userId);
                            performLogin(savedEmail, token, true);
                        } else {
                            showCustomToast("User does not exist.", 1000);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error parsing customer data: " + e.getMessage());
                        showCustomToast("Error parsing customer data: " + e.getMessage(), 1000);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Failed to fetch customer: " + errorBody);
                        showCustomToast("Failed to fetch customer: " + errorBody, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                        showCustomToast("Error reading error body: " + e.getMessage(), 1000);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error or other error: " + t.getMessage());
                showCustomToast("Failed to check customer existence: " + t.getMessage(), 1000);
            }
        });
    }

    private void performLogin(String email, String token, boolean rememberMe) {
        Log.d(TAG, "Attempting to login with email: " + email);

        Intent intent = new Intent(LoginPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT);
        toast.show();

        new android.os.Handler().postDelayed(toast::cancel, duration);
    }
}
