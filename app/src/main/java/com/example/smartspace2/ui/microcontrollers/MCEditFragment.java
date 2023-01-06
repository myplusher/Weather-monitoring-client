package com.example.smartspace2.ui.microcontrollers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentMcEditBinding;

public class MCEditFragment extends Fragment {
    private FragmentMcEditBinding binding;

    @SuppressLint("ResourceType")
    public View onCreate(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        MicrocontrollerViewModel microcontrollerViewModel =
                new ViewModelProvider(this).get(MicrocontrollerViewModel.class);

        binding = FragmentMcEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root.findViewById(R.id.edit_mc_address_field);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
