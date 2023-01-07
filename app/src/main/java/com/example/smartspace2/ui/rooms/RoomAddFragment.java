package com.example.smartspace2.ui.rooms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartspace2.R;
import com.example.smartspace2.databinding.FragmentAddRoomBinding;
import com.example.smartspace2.databinding.FragmentSettingsBinding;
import com.example.smartspace2.dto.LocationDto;
import com.example.smartspace2.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomAddFragment extends Fragment {
    private FragmentAddRoomBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText nameInput = root.findViewById(R.id.inputNameRoomCreate);
        EditText contentInput = root.findViewById(R.id.inputContentRoomCreate);
        Button button = root.findViewById(R.id.button6);

        button.setOnClickListener(view -> {
            createRoom(new LocationDto(nameInput.getText().toString(), Double.parseDouble(contentInput.getText().toString())));
        });
        return root;
    }

    public void createRoom(LocationDto loc) {
        Activity activity = getActivity();
        NetworkService.getInstance()
                .getJSONApi()
                .createLocation(loc)
                .enqueue(new Callback<LocationDto>() {
                    @Override
                    public void onResponse(Call<LocationDto> call, Response<LocationDto> response) {
                        Toast toast = Toast.makeText(activity, "Сохранено", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<LocationDto> call, Throwable t) {
                        Toast toast = Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }


}
