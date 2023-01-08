package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentMcEditBinding;
import com.example.smartspace2.dto.LocationDto;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MCEditFragment extends Fragment {
    private FragmentMcEditBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        MicrocontrollerViewModel microcontrollerViewModel =
                new ViewModelProvider(this).get(MicrocontrollerViewModel.class);

        binding = FragmentMcEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root.findViewById(R.id.edit_mc_address_field);

        TextView title = root.findViewById(R.id.edit_mc_name);
        EditText addressField = root.findViewById(R.id.edit_mc_address_field);
        EditText shortNameField = root.findViewById(R.id.edit_mc_short_name_field);
        TextView currentLoc = root.findViewById(R.id.currentloc);
        Button saveBtn = root.findViewById(R.id.mc_save);
        Spinner spinner = root.findViewById(R.id.spinner);




        Activity activity = getActivity();
        NetworkService.getInstance()
                .getJSONApi()
                .getLocations()
                .enqueue(new Callback<LocationDto[]>() {
                    @Override
                    public void onResponse(Call<LocationDto[]> call, Response<LocationDto[]> response) {
                        LocationDto[] locations = response.body();
                        List<String> options = new ArrayList<>();
                        for (LocationDto loc : locations) {
                            options.add(loc.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, options);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<LocationDto[]> call, Throwable t) {
                        Toast toast = Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });



        int id = 0;
        String address = "";
        String shortName = "";
        int locID = 0;
        String locName = "";
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            address = getArguments().getString("address");
            shortName = getArguments().getString("shortName");
            locID = getArguments().getInt("shortID");
            locName = getArguments().getString("locName");
        }
        title.setText(String.valueOf(id));
        addressField.setText(address);
        shortNameField.setText(shortName);
        currentLoc.setText(locName);


        int finalId = id;
        saveBtn.setOnClickListener(view -> {
            saveMC(finalId, addressField.getText().toString(), shortNameField.getText().toString(),
                    spinner.getSelectedItem().toString(), root);
        });

        return root;
    }

    public void saveMC(int id, String address, String shortName, String room, View view) {
        Activity activity = getActivity();

        MCDto mcDto = new MCDto(id, address, shortName, room);
        NetworkService.getInstance()
                .getJSONApi()
                .updateMC(id, mcDto)
                .enqueue(new Callback<MCDto>() {
                    @Override
                    public void onResponse(Call<MCDto> call, Response<MCDto> response) {
                        Toast toast = Toast.makeText(activity, "Сохранено", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<MCDto> call, Throwable t) {
                        Toast toast = Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG);
                        toast.show();

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
