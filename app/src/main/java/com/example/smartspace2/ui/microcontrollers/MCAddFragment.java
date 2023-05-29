package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentAddMcBinding;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.NetworkService;
import com.google.android.material.snackbar.Snackbar;
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
        NetworkService.getInstance()
                .getJSONApi()
                .createMC(mc)
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
}
