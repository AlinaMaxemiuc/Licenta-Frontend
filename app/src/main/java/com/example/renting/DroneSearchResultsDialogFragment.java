package com.example.renting;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class DroneSearchResultsDialogFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private DroneAdapter adapter;
    private List<DroneData> droneList;

    public static DroneSearchResultsDialogFragment newInstance(List<DroneData> drones) {
        DroneSearchResultsDialogFragment fragment = new DroneSearchResultsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("drones", (java.io.Serializable) drones);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_results, container, false);

        recyclerView = view.findViewById(R.id.dialog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            droneList = (List<DroneData>) getArguments().getSerializable("drones");
            adapter = new DroneAdapter(getContext(), droneList, true, getParentFragmentManager());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
