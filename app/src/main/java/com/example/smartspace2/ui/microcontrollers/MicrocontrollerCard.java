package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smartspace2.R;
import com.example.smartspace2.dto.MCDto;

import org.jetbrains.annotations.NotNull;

public class MicrocontrollerCard extends Fragment {
    private int id;
    private String address;
    private String shortName;
    private String locName;
    private int locID;

    public MicrocontrollerCard(int id, String address, String shortName, String locName, int locID) {
        this.id = id;
        this.address = address;
        this.shortName = shortName;
        this.locID = locID;
        this.locName = locName;
    }

    public MicrocontrollerCard(MCDto item) {
        this.id = item.getId();
        this.address = item.getAddress();
        this.shortName = item.getShortname();
        this.locID = item.getLocationID();
        this.locName = item.getLocationName();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mc_card,
                container, false);

        TextView cardTitle = v.findViewById(R.id.id);
        cardTitle.setText(String.valueOf(id));

        TextView addressTemp = v.findViewById(R.id.address);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView shortNameTemp = v.findViewById(R.id.short_name);
        addressTemp.setText(address);
        shortNameTemp.setText(shortName);

        Button settings = v.findViewById(R.id.button);
        settings.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putString("address", address);
            bundle.putString("shortName", shortName);
            bundle.putInt("locID", locID);
            bundle.putString("locName", locName);
            NavHostFragment.findNavController(MicrocontrollerCard.this)
                    .navigate(R.id.action_navigation_microcontroller_to_mc_edit, bundle);
        });

        return v;
    }

}
