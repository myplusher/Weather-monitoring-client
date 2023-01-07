package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentAddMcBinding;
import com.example.smartspace2.databinding.FragmentAddRoomBinding;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MCAddFragment extends Fragment {
    private FragmentAddMcBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddMcBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText addressInput = root.findViewById(R.id.inputAddressMCCreate);
        EditText shortNameInput = root.findViewById(R.id.inputShortNameMCCreate);
        Button button = root.findViewById(R.id.button5);

        button.setOnClickListener(view -> {
            createMC(new MCDto(addressInput.getText().toString(), shortNameInput.getText().toString()));
        });

        return root;
    }

    public void createMC(MCDto mc) {
        Activity activity = getActivity();
        NetworkService.getInstance()
                .getJSONApi()
                .createMC(mc)
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
}
