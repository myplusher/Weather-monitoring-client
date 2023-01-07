package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentMcEditBinding;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.ApiResponse;
import com.example.smartspace2.service.NetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        Button saveBtn = root.findViewById(R.id.mc_save);

        int id = 0;
        String address = "";
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            address = getArguments().getString("address");
        }
        title.setText(String.valueOf(id));
        addressField.setText(address);

        Activity activity = getActivity();

        int finalId = id;
        saveBtn.setOnClickListener(view -> {
            saveMC(finalId, addressField.getText().toString(), root);
        });

        return root;
    }

    public void saveMC(int id, String address, View view) {
        Activity activity = getActivity();
        // todo дописать шорнейм
        NetworkService.getInstance()
                .getJSONApi()
                .updateMC(id, new MCDto(id, address, ""))
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
