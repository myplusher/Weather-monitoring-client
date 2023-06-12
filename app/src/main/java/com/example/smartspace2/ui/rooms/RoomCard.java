package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smartspace2.R;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.ui.bottom_sheet.BottomSheetDialog;

import com.google.android.material.card.MaterialCardView;
import org.jetbrains.annotations.NotNull;

public class RoomCard extends Fragment {
    private int id;
    private String title;
    private double temp;
    private double co2;
    private double humidity;
    private double light;

    public RoomCard(RoomDto roomDto) {
        this.id = roomDto.getMicrocontrollerId().getId();
        this.title = roomDto.getTitle();
        this.temp = roomDto.getTemperature();
        this.co2 = roomDto.getCo2();
        this.humidity = roomDto.getHumidity();
        this.light = roomDto.getLight();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.room_card,
                container, false);

        TextView cardTitle = v.findViewById(R.id.card_title);
        cardTitle.setText(title);

        TextView cardTemp = v.findViewById(R.id.card_temp);
        cardTemp.setText(String.format("%.1f", temp) + "°");

        TextView cardHumidity = v.findViewById(R.id.card_humidity);
        cardHumidity.setText(String.format("%.0f%%", humidity));

        TextView cardCo2 = v.findViewById(R.id.card_co2);
        cardCo2.setText(String.format("%.0f", co2));

        TextView cardLight = v.findViewById(R.id.card_light);
        cardLight.setText(String.format("%.0f", light));

        MaterialCardView card = v.findViewById(R.id.card);
        setColors(v);
        card.setOnClickListener(view -> {
            BottomSheetDialog bottomSheet = new BottomSheetDialog(String.valueOf(id), title);
            bottomSheet.show(getParentFragmentManager(),
                    "ModalBottomSheet");
        });

        return v;
    }

    private void setColors(View v) {
        Context context = getContext();
        View tempLayout = v.findViewById(R.id.temp_layout);
        if ((temp > 22 && temp < 23) || (temp > 25 && temp < 26))
            tempLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.amber_50));
        else if (temp < 22 || temp > 25)
            tempLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_50));

        View humidityLayout = v.findViewById(R.id.humidity_layout);
        if ((humidity > 40 && humidity < 46) || (humidity > 55 && humidity < 61))
            humidityLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.amber_50));
        else if (humidity < 40 || humidity > 60)
            humidityLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_50));

        View co2Layout = v.findViewById(R.id.co2_layout);
        if ((co2 > 800 && co2 < 850) || (co2 > 1300 && co2 < 1401))
            co2Layout.setBackgroundColor(ContextCompat.getColor(context, R.color.amber_50));
        else if (co2 < 800 || co2 > 1400)
            co2Layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_50));

        View lightLayout = v.findViewById(R.id.light_layout);
        if ((light > 50 && light < 60) || (light > 190 && light < 201))
            lightLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.amber_50));
        else if (light < 50 || light > 200)
            lightLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_50));
    }
}
