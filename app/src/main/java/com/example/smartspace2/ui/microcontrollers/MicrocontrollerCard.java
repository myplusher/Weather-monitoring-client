package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smartspace2.R;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.ui.bottom_sheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

public class MicrocontrollerCard extends Fragment {
    private String id;
    private String address;

    public MicrocontrollerCard(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public MicrocontrollerCard(MCDto item) {
        this.id = item.getId();
        this.address = item.getAddress();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mc_card,
                container, false);

        TextView cardTitle = v.findViewById(R.id.id);
        cardTitle.setText(id);

        TextView addressTemp = v.findViewById(R.id.address);
        addressTemp.setText(address);

        Button settings = v.findViewById(R.id.button);
        settings.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("address", address);
            NavHostFragment.findNavController(MicrocontrollerCard.this)
                    .navigate(R.id.action_microcontrollerCard_to_MCEditFragment, bundle);
        });

        return v;
    }

    public void recreate() {

    }
}
