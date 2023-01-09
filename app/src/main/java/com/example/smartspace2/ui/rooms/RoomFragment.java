package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.smartspace2.databinding.FragmentRoomBinding;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.dto.RoomListDto;
import com.example.smartspace2.service.NetworkService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            binding.refreshRoomLayout.setRefreshing(false);
        });

        return root;
    }

    private void getRoomCards(LinearLayout linearLayout) {
        Activity activity = getActivity();
        FragmentManager parentFragmentManager = getParentFragmentManager();
        linearLayout.removeAllViews();
        NetworkService.getInstance()
                .getJSONApi()
                .getData()
                .enqueue(new Callback<RoomDto[]>() {
                    @Override
                    public void onResponse(Call<RoomDto[]> call, Response<RoomDto[]> response) {
                        if (!response.isSuccessful()) {
                            Toast toast;
                            switch (response.code()) {
                                case 404:
                                    toast = Toast.makeText(activity, "Неверный запрос", Toast.LENGTH_LONG);
                                    toast.show();
                                    break;
                                case 500:
                                    toast = Toast.makeText(activity, "Ошибка на сервере", Toast.LENGTH_LONG);
                                    toast.show();
                                    break;
                                default:
                                    toast = Toast.makeText(activity, "Непредвиденная ошибка", Toast.LENGTH_LONG);
                                    toast.show();
                                    break;
                            }
                            return;
                        }
                        RoomDto[] roomDtoList = response.body();
                        if (roomDtoList != null && roomDtoList.length != 0) {
                            for (RoomDto rDto : roomDtoList) {
                                parentFragmentManager.beginTransaction()
                                        .setReorderingAllowed(true)
                                        .add(linearLayout.getId(), new RoomCard(rDto))
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RoomDto[]> call, Throwable t) {
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