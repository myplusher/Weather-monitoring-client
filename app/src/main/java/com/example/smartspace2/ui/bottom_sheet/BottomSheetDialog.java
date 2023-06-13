package com.example.smartspace2.ui.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import com.example.smartspace2.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private String title;
    private String roomId;

    public BottomSheetDialog(String roomId, String title) {
        this.roomId = roomId;
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);
        TextView viewTitle = v.findViewById(R.id.bottom_sheet_title);
        viewTitle.setText(title);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button historyBtn = view.findViewById(R.id.btn_history);

        historyBtn.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", roomId);
            bundle.putString("name", title);
            NavHostFragment.findNavController(BottomSheetDialog.this)
                    .navigate(R.id.action_bottomSheetDialog_to_historyFragment, bundle);
            this.dismiss();
        });

        Button reportBtn = view.findViewById(R.id.btn_report);

        reportBtn.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", roomId);
            bundle.putString("name", title);
            NavHostFragment.findNavController(BottomSheetDialog.this)
                    .navigate(R.id.action_bottomSheetDialog_to_reportFragment, bundle);
            this.dismiss();
        });
    }
}
