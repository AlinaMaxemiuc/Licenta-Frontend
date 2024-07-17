package com.example.renting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordDialogFragment extends DialogFragment {

    private EditText editTextEmail, editTextNewPassword;
    private Button buttonResetPassword;
    private ApiInterface apiInterface;

    public static ResetPasswordDialogFragment newInstance() {
        return new ResetPasswordDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reset_password, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        buttonResetPassword = view.findViewById(R.id.buttonResetPassword);

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String newPassword = editTextNewPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(getActivity(), "Both fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                fetchAndResetPassword(email, newPassword);
            }
        });

        return view;
    }

    private void fetchAndResetPassword(String email, String newPassword) {
        Call<UserResponse> call = apiInterface.getUserByEmail(email);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userId = response.body().getId();
                    updatePassword(userId, newPassword);
                } else {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to fetch user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword(String userId, String newPassword) {
        PasswordResetRequest request = new PasswordResetRequest(userId, newPassword);
        Call<ResponseBody> call = apiInterface.updatePassword(userId, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Password reset successful", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Password reset failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
