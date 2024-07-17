package com.example.renting;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import android.view.Window;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

public class UnavailableDroneDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE = "message";

    public static UnavailableDroneDialogFragment newInstance(String message) {
        UnavailableDroneDialogFragment fragment = new UnavailableDroneDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unavailable_drone_dialog, container, false);

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieSadAnimation);
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        Button okButton = view.findViewById(R.id.okButton);

        String message = getArguments().getString(ARG_MESSAGE);
        messageTextView.setText(message);

        okButton.setOnClickListener(v -> dismiss());

        lottieAnimationView.playAnimation();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
