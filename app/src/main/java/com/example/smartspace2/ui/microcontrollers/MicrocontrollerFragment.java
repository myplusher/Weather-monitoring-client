package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartspace2.databinding.FragmentMicrocontrollerBinding;
import com.example.smartspace2.dto.MCDto;
import com.example.smartspace2.dto.MCListDto;
import com.example.smartspace2.dto.RoomListDto;
import com.example.smartspace2.ui.rooms.RoomCard;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        AsyncTask.execute(() -> {
            HttpURLConnection myConnection = null;
            // Create URL
            try {
                URL url = new URL("http://192.168.1.143:8080/controllers");
                myConnection =
                        (HttpURLConnection) url.openConnection();
                myConnection.setRequestMethod("GET");
                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    ObjectMapper mapper = new ObjectMapper();
                    MCDto[] mcListDto = mapper.readValue(responseBodyReader, MCDto[].class);
                    if (mcListDto != null && mcListDto.length != 0) {
                        for (MCDto mcDto : mcListDto) {
                            parentFragmentManager.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(linearLayout.getId(), new MicrocontrollerCard(mcDto))
                                    .commit();
                        }
                    }
                } else {
                    //todo обработка ошибки
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (myConnection != null)
                    myConnection.disconnect();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}