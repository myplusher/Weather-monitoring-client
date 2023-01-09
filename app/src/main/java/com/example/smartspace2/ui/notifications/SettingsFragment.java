package com.example.smartspace2.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentSettingsBinding;
import com.example.smartspace2.ui.microcontrollers.MicrocontrollerCard;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button addRoomBtn = root.findViewById(R.id.button2);
        Button addMcBtn = root.findViewById(R.id.button3);

        addRoomBtn.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.action_navigation_settings_to_roomAddFragment, bundle);
        });

        addMcBtn.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.action_navigation_settings_to_MCAddFragment2, bundle);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}