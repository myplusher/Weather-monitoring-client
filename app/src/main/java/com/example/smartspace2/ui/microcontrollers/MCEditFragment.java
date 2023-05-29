package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentMcEditBinding;
import com.example.smartspace2.dto.LocationDto;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        if (!response.isSuccessful()) {
                            switch (response.code()) {
                                case 404:
                                    Snackbar.make(root, "Неверный запрос", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Snackbar.make(root, "Ошибка на сервере", Snackbar.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Snackbar.make(root, "Непредвиденная ошибка", Snackbar.LENGTH_SHORT).show();
                                    break;
                            }
                            return;
                        }

                        LocationDto[] locations = response.body();
                        List<String> options = Stream.of(locations)
                                .map(LocationDto::getName)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, options);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<LocationDto[]> call, Throwable t) {
                        Snackbar.make(root, t.toString(), Snackbar.LENGTH_SHORT).show();
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
            locID = getArguments().getInt("locID");
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
        MCDto mcDto = new MCDto(id, address, shortName, room);
        NetworkService.getInstance()
                .getJSONApi()
                .updateMC(id, mcDto)
                .enqueue(new Callback<MCDto>() {
                    @Override
                    public void onResponse(Call<MCDto> call, Response<MCDto> response) {
                        if (!response.isSuccessful()) {
                            switch (response.code()) {
                                case 404:
                                    Snackbar.make(binding.getRoot(), "Неверный запрос", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Snackbar.make(binding.getRoot(), "Ошибка на сервере", Snackbar.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Snackbar.make(binding.getRoot(), "Непредвиденная ошибка", Snackbar.LENGTH_SHORT).show();
                                    break;
                            }
                            return;
                        }
                        Snackbar.make(binding.getRoot(), "Сохранено", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MCDto> call, Throwable t) {
                        Snackbar.make(binding.getRoot(), t.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
