package com.example.smartspace2.ui.rooms;

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

import com.example.smartspace2.R;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.ui.bottom_sheet.BottomSheetDialog;

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
        this.title = roomDto.getMicrocontrollerId().getLocationName();
        this.temp = roomDto.getTemperature();
        this.co2 = roomDto.getCo2();
        this.humidity = roomDto.getHumidity();
        this.light = roomDto.getLight();
    }

    @SuppressLint("SetTextI18n")
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
        cardHumidity.setText(String.format("%.0f", humidity) + "%");

        TextView cardCo2 = v.findViewById(R.id.card_co2);
        cardCo2.setText(String.valueOf(String.format("%.0f", co2)));

        TextView cardLight = v.findViewById(R.id.card_light);
        cardLight.setText(String.valueOf(String.format("%.0f", light)));

        Button settings = v.findViewById(R.id.btn_settings);
        settings.setOnClickListener(view -> {
            BottomSheetDialog bottomSheet = new BottomSheetDialog(String.valueOf(id), title);
            bottomSheet.show(getParentFragmentManager(),
                    "ModalBottomSheet");
        });

        return v;
    }
}
