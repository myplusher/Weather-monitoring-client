package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.smartspace2.databinding.FragmentRoomBinding;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.dto.RoomListDto;
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
        FragmentManager parentFragmentManager = getParentFragmentManager();
        linearLayout.removeAllViews();
        AsyncTask.execute(() -> {
            HttpURLConnection myConnection = null;
            // Create URL
            try {
                URL githubEndpoint = new URL("http://192.168.1.143:8080/rooms");
                // Create connection
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
                myConnection.setRequestMethod("GET");
                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    ObjectMapper mapper = new ObjectMapper();
                    RoomDto[] roomList = mapper.readValue(responseBodyReader, RoomDto[].class);
                    if (roomList != null && roomList.length != 0) {
                        for (RoomDto rDto : roomList) {
                            parentFragmentManager.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(linearLayout.getId(), new RoomCard(rDto))
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