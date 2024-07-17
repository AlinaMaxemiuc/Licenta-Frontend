package com.example.renting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RateActivity extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rate, container, false);

        TextView title = view.findViewById(R.id.dialog_title);
        title.setText("Rate this app :)");

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            showCustomToast("You rated: " + rating, 1000);
            showCustomToast("Thank you for rating our app!", 1000);
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showCustomToast(String message, int duration) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();

        new Handler(Looper.getMainLooper()).postDelayed(toast::cancel, duration);
    }
}
