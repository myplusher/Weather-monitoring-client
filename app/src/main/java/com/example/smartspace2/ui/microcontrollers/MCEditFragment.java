package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentMcEditBinding;
import com.example.smartspace2.dto.MCDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        int finalId = id;
        saveBtn.setOnClickListener(view -> {
            saveMC(finalId, addressField.getText().toString());
        });

        return root;
    }

    public void saveMC(int id, String address) {
        AsyncTask.execute(() -> {
            HttpURLConnection con = null;
            // Create URL
            try {
                URL url = new URL("http://192.168.1.143:8080/controllers/"+id);
                con =
                        (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded" );
                try(OutputStream os = con.getOutputStream()) {
                    String body = "{\"id\":\"" + id + "\",\"address\":\"" + address + "\"}";
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                con.setRequestMethod("PUT");



                if (con.getResponseCode() == 200) {
                    InputStream responseBody = con.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    ObjectMapper mapper = new ObjectMapper();
                    MCDto[] mcListDto = mapper.readValue(responseBodyReader, MCDto[].class);

                } else {
                    //todo обработка ошибки
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (con != null)
                    con.disconnect();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
