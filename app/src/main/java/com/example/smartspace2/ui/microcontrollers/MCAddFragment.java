package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentAddMcBinding;
import com.example.smartspace2.databinding.FragmentAddRoomBinding;

public class MCAddFragment extends Fragment {
    private FragmentAddMcBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddMcBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }
}
