package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.example.smartspace2.databinding.FragmentMicrocontrollerBinding;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.service.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MicrocontrollerFragment extends Fragment {

    private FragmentMicrocontrollerBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MicrocontrollerViewModel microcontrollerViewModel =
                new ViewModelProvider(this).get(MicrocontrollerViewModel.class);

        binding = FragmentMicrocontrollerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        layoutParams.setMargins(0, 0, 0, 0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(1224);

        binding.mcScroll.addView(linearLayout);
        getMC(linearLayout);

        binding.refreshRoomLayout.setOnRefreshListener(() -> {
            getMC(linearLayout);
            binding.refreshRoomLayout.setRefreshing(false);
        });


        return root;
    }

    private void getMC(LinearLayout linearLayout) {
        FragmentManager parentFragmentManager = getParentFragmentManager();
        linearLayout.removeAllViews();
        NetworkService.getInstance()
                .getJSONApi()
                .getMCList()
                .enqueue(new Callback<MCDto[]>() {
                    @Override
                    public void onResponse(Call<MCDto[]> call, Response<MCDto[]> response) {
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
                        MCDto[] mcListDto = response.body();
                        if (mcListDto != null && mcListDto.length != 0) {
                            for (MCDto mcDto : mcListDto) {
                                parentFragmentManager.beginTransaction()
                                        .setReorderingAllowed(true)
                                        .add(linearLayout.getId(), new MicrocontrollerCard(mcDto))
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MCDto[]> call, Throwable t) {
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