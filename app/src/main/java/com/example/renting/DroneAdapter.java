package com.example.renting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DroneAdapter extends RecyclerView.Adapter<DroneAdapter.DroneViewHolder> {
    private Context context;
    private List<DroneData> droneList;
    private boolean showImages;
    private FragmentManager fragmentManager;

    public DroneAdapter(Context context, List<DroneData> droneList, boolean showImages) {
        this.context = context;
        this.droneList = droneList;
        this.showImages = showImages;
    }

    public DroneAdapter(Context context, List<DroneData> droneList, boolean showImages, FragmentManager fragmentManager) {
        this.context = context;
        this.droneList = droneList;
        this.showImages = showImages;
        this.fragmentManager = fragmentManager;
    }

    public void setSearchList(List<DroneData> dataSearchList) {
        this.droneList = dataSearchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DroneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new DroneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DroneViewHolder holder, int position) {
        DroneData drone = droneList.get(position);
        Log.d("DroneAdapter", "Drone ID: " + drone.getId());

        int droneImageResId = getDroneImageResId(drone.getId());
        Log.d("DroneAdapter", "Drone Image Res ID: " + droneImageResId);

        if (showImages) {
            holder.recImage.setImageResource(droneImageResId);
        } else {
            holder.recImage.setVisibility(View.GONE);
        }

        holder.recTitle.setText(drone.getName());
        holder.recModel.setText("Model: " + drone.getModel());
        holder.recUtility.setText("Utility: " + drone.getUtilityAsString());
        holder.recCategory.setText("Category: " + drone.getCategoryAsString());
        holder.recProductionYear.setText("Production year: " + extractYearFromDate(drone.getProductionYear()));

        holder.recCard.setOnClickListener(v -> {
            if (fragmentManager != null) {
                DroneDetailsDialogFragment dialogFragment = DroneDetailsDialogFragment.newInstance(drone, droneImageResId);
                dialogFragment.show(fragmentManager, "dialog_drone_details");
            }
        });
    }

    @Override
    public int getItemCount() {
        return droneList.size();
    }

    public class DroneViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recTitle, recModel, recUtility, recCategory, recProductionYear;
        CardView recCard;

        public DroneViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recModel = itemView.findViewById(R.id.recModel);
            recUtility = itemView.findViewById(R.id.recUtility);
            recCategory = itemView.findViewById(R.id.recCategory);
            recProductionYear = itemView.findViewById(R.id.recProductionYear);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }

    private int getDroneImageResId(String droneId) {
        Log.d("DroneAdapter", "getDroneImageResId for Drone ID: " + droneId);
        switch (droneId.toUpperCase()) {
            case "98031C55-F7D3-4DF9-8068-3A130E979846":
                return R.drawable.djiminitwo;
            case "B6118A69-0722-938A-8635-3A1333FC1979":
                return R.drawable.djitello;
            case "68304F1E-DAD0-867D-F4AB-3A133998A498":
                return R.drawable.djiagras;
            case "2B47B9FD-4BA3-6DB3-759E-3A13399C893A":
                return R.drawable.foldablepro;
            case "272D0DC8-4CCD-BE90-FCC9-3A1339A05372":
                return R.drawable.maxavoidance;
            case "AC5F65E3-6D11-1C90-6DCB-3A1339A24BF9":
                return R.drawable.zeevos;
            default:
                return R.drawable.logonobg;
        }
    }

    private String extractYearFromDate(String date) {
        if (date != null && date.length() >= 4) {
            return date.substring(0, 4);
        }
        return "";
    }
}
