package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartspace2.databinding.FragmentAddRoomBinding;
import com.example.smartspace2.databinding.FragmentSettingsBinding;

public class RoomAddFragment extends Fragment {
    private FragmentAddRoomBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }


}
