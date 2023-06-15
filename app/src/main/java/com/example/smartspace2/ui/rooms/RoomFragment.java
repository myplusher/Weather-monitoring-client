package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.smartspace2.databinding.*;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.service.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class RoomFragment extends Fragment {

    private FragmentRoomBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        layoutParams.setMargins(0, 0, 0, 0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(1224);

        binding.roomScroll.addView(linearLayout);

        getRoomCards(linearLayout);

        binding.refreshRoomLayout.setOnRefreshListener(() -> {
            getRoomCards(linearLayout);
        });

        return root;
    }

    private void getRoomCards(LinearLayout linearLayout) {
        binding.refreshRoomLayout.setRefreshing(true);
        FragmentManager parentFragmentManager = getParentFragmentManager();
        linearLayout.removeAllViews();
        NetworkService.getInstance()
                .getJSONApi()
                .getData()
                .enqueue(new Callback<RoomDto[]>() {
                    @Override
                    public void onResponse(Call<RoomDto[]> call, Response<RoomDto[]> response) {
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
                            binding.refreshRoomLayout.setRefreshing(false);
                            return;
                        }
                        try {
                            RoomDto[] roomDtoList = response.body();
                            if (roomDtoList != null && roomDtoList.length != 0) {
                                for (RoomDto rDto : roomDtoList) {
                                    RoomCard roomCard = new RoomCard(rDto);
                                    /*roomCard.setCo*/
                                    parentFragmentManager.beginTransaction()
                                            .setReorderingAllowed(true)
                                            .add(linearLayout.getId(), roomCard)
                                            .commit();
                                }
                            }
                        } finally {
                            binding.refreshRoomLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<RoomDto[]> call, Throwable t) {
                        binding.refreshRoomLayout.setRefreshing(false);
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