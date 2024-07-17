package com.example.renting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentalsAdapter extends RecyclerView.Adapter<RentalsAdapter.RentalViewHolder> {

    private Context context;
    private List<RentalData> rentalList;
    private Map<String, DroneData> droneDataMap;
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public RentalsAdapter(Context context, List<RentalData> rentalList, Map<String, DroneData> droneDataMap) {
        this.context = context;
        this.rentalList = rentalList;
        this.droneDataMap = droneDataMap;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rental, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        RentalData rental = rentalList.get(position);

        holder.cardNumberTextView.setText(String.format("Rental #%d", position + 1));

        if (!rental.getRentalItems().isEmpty()) {
            RentalItem rentalItem = rental.getRentalItems().get(0);
            DroneData droneData = droneDataMap.get(rentalItem.getDroneId());

            if (droneData != null) {
                holder.droneNameTextView.setText("Name: " + droneData.getName());
                holder.droneModelTextView.setText("Model: " + droneData.getModel());
                Drawable droneImage = getDroneImageDrawable(droneData.getId());
                holder.droneImageView.setImageDrawable(droneImage);
            } else {
                holder.droneNameTextView.setText("Drone name not found");
                holder.droneModelTextView.setText("Drone model not found");
                holder.droneImageView.setImageResource(R.drawable.logonobg);
            }
        } else {
            holder.droneNameTextView.setText("No drone rented");
            holder.droneModelTextView.setText("");
            holder.droneImageView.setImageResource(R.drawable.logonobg);
        }

        try {
            Date startDate = apiDateFormat.parse(rental.getStartDay());
            Date endDate = apiDateFormat.parse(rental.getEndDay());

            holder.startDateTextView.setText("Start date: " + displayDateFormat.format(startDate));
            holder.endDateTextView.setText("End date: " + displayDateFormat.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.totalTextView.setText(String.format(Locale.getDefault(), "Total: %.2f", rental.getTotal()));
        holder.paymentMethodTextView.setText("Payment method: " + rental.getPaymentMethodAsString());
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    static class RentalViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumberTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView totalTextView;
        TextView paymentMethodTextView;
        TextView droneNameTextView;
        TextView droneModelTextView;
        ImageView droneImageView;

        RentalViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumberTextView = itemView.findViewById(R.id.cardNumberTextView);
            droneNameTextView = itemView.findViewById(R.id.droneNameTextView);
            droneModelTextView = itemView.findViewById(R.id.droneModelTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
            droneImageView = itemView.findViewById(R.id.droneImageView);
        }
    }

    private Drawable getDroneImageDrawable(String droneId) {
        int droneImageResId;
        switch (droneId.toUpperCase()) {
            case "98031C55-F7D3-4DF9-8068-3A130E979846":
                droneImageResId = R.drawable.djiminitwo;
                break;
            case "B6118A69-0722-938A-8635-3A1333FC1979":
                droneImageResId = R.drawable.djitello;
                break;
            case "68304F1E-DAD0-867D-F4AB-3A133998A498":
                droneImageResId = R.drawable.djiagras;
                break;
            case "2B47B9FD-4BA3-6DB3-759E-3A13399C893A":
                droneImageResId = R.drawable.foldablepro;
                break;
            case "272D0DC8-4CCD-BE90-FCC9-3A1339A05372":
                droneImageResId = R.drawable.maxavoidance;
                break;
            case "AC5F65E3-6D11-1C90-6DCB-3A1339A24BF9":
                droneImageResId = R.drawable.zeevos;
                break;
            default:
                droneImageResId = R.drawable.logonobg;
        }
        return ContextCompat.getDrawable(context, droneImageResId);
    }
}
