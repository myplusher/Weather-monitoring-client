package com.example.smartspace2.ui.history;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.smartspace2.R;
import com.example.smartspace2.dto.HistoryItem;
import com.example.smartspace2.dto.RoomHistoryDto;
import com.example.smartspace2.service.NetworkService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoryFragment extends Fragment {

    private String roomId;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        String id = Optional.ofNullable(getArguments())
                .map(bundle -> bundle.getString("id"))
                .orElse(null);

        Optional.ofNullable(getArguments())
                .map(bundle -> bundle.getString("name"))
                .ifPresent(((AppCompatActivity) getActivity()).getSupportActionBar()::setSubtitle);

        TextView selectedDate = root.findViewById(R.id.show_selected_date);
        SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        MaterialDatePicker.Builder<Pair<Long, Long>> rangePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        long initStartTime = MaterialDatePicker.thisMonthInUtcMilliseconds();
        long initEndTime = MaterialDatePicker.todayInUtcMilliseconds();
        rangePickerBuilder.setSelection(new Pair<>(
                initStartTime,
                initEndTime
        ));
        MaterialDatePicker<Pair<Long, Long>> rangePicker = rangePickerBuilder.build();
        List<ILineDataSet> dataSets = new ArrayList<>();
        rangePicker.addOnPositiveButtonClickListener(selection -> {
            Date start = new Date(selection.first);
            Date end = new Date(selection.second);

            String viewText = viewDateFormat.format(start) + " - " + viewDateFormat.format(end);
            selectedDate.setText(viewText);
            sendData(root, id, start, end, dataSets);
        });

        Button rangeBtn = root.findViewById(R.id.btn_range);
        rangeBtn.setOnClickListener(view -> rangePicker.show(getParentFragmentManager(), "tag"));

        Button buttonTemp = root.findViewById(R.id.buttonTemp);
        buttonTemp.setOnClickListener(v -> updateChart(root, dataSets));

        Button buttonHumidity = root.findViewById(R.id.buttonHumidity);
        buttonHumidity.setOnClickListener(v -> updateChart(root, dataSets));

        Button buttonCO2 = root.findViewById(R.id.buttonCO2);
        buttonCO2.setOnClickListener(v -> updateChart(root, dataSets));

        Button buttonLight = root.findViewById(R.id.buttonLight);
        buttonLight.setOnClickListener(v -> updateChart(root, dataSets));

        Date initStartDate = new Date(initStartTime);
        Date initEndDate = new Date(initEndTime);
        String viewText = viewDateFormat.format(initStartDate) + " - " + viewDateFormat.format(initEndDate);
        selectedDate.setText(viewText);
        sendData(root, id, initStartDate, initEndDate, dataSets);

        return root;
    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        super.onDestroyView();
    }

    private void sendData(View root, String id, Date startDate, Date endDate, List<ILineDataSet> dataSets) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sendDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = sendDateFormat.format(startDate);
        String end = sendDateFormat.format(endDate);
        LineChart lineChart = root.findViewById(R.id.chart);
        LinearProgressIndicator progressIndicator = root.findViewById(R.id.load_chart_progress);
        progressIndicator.setIndeterminate(true);
        NetworkService.getInstance()
                .getJSONApi()
                .getHistory(Integer.parseInt(id), start, end)
                .enqueue(new Callback<RoomHistoryDto>() {
                    @Override
                    public void onResponse(Call<RoomHistoryDto> call, Response<RoomHistoryDto> response) {
                        if (!response.isSuccessful()) {
                            switch (response.code()) {
                                case 404:
                                    Snackbar.make(lineChart, "Неверный запрос", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Snackbar.make(lineChart, "Ошибка на сервере", Snackbar.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Snackbar.make(lineChart, "Непредвиденная ошибка", Snackbar.LENGTH_SHORT).show();
                                    break;
                            }
                            progressIndicator.setIndeterminate(false);
                            return;
                        }
                        try {
                            dataSets.clear();

                            RoomHistoryDto historyDto = response.body();

                            List<HistoryItem> temperatureHistory = historyDto.getTemperatureHistory();
                            Optional.ofNullable(getLineDataSet(temperatureHistory, "°C", Color.RED, 1))
                                    .ifPresent(dataSets::add);;

                            List<HistoryItem> humidityHistory = historyDto.getHumidityHistory();
                            Optional.ofNullable(getLineDataSet(humidityHistory, "φ(%)", Color.BLUE, 1))
                                    .ifPresent(dataSets::add);

                            List<HistoryItem> co2History = historyDto.getCo2History();
                            Optional.ofNullable(getLineDataSet(co2History, "CO2(ppm*10)", Color.GRAY, 10))
                                    .ifPresent(dataSets::add);

                            List<HistoryItem> lightHistory = historyDto.getLightHistory();
                            Optional.ofNullable(getLineDataSet(lightHistory, "E(*10)", Color.GREEN, 10))
                                    .ifPresent(dataSets::add);

                            updateChart(root, dataSets);
                        } finally {
                            progressIndicator.setIndeterminate(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<RoomHistoryDto> call, Throwable t) {
                        progressIndicator.setIndeterminate(false);
                        Snackbar.make(lineChart, t.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private static LineDataSet getLineDataSet(List<HistoryItem> historyItems, String label, int color, int divider) {
        if (divider < 1)
            divider = 1;

        LineDataSet dataset = null;
        if (historyItems != null && historyItems.size() > 0) {
            int finalDivider = divider;
            List<Entry> entriesH = historyItems.stream()
                    .map(item -> new Entry(item.getDate(), Float.parseFloat(item.getValue()) / finalDivider))
                    .collect(Collectors.toList());
            dataset = new LineDataSet(entriesH, label);
            dataset.setColor(color);
        }
        return dataset;
    }

    private void updateChart(View root, List<ILineDataSet> dataSets) {
        Button buttonTemp = root.findViewById(R.id.buttonTemp);
        boolean buttonTempSelected = buttonTemp.createAccessibilityNodeInfo().isChecked();

        Button buttonHumidity = root.findViewById(R.id.buttonHumidity);
        boolean buttonHumiditySelected = buttonHumidity.createAccessibilityNodeInfo().isChecked();

        Button buttonCO2 = root.findViewById(R.id.buttonCO2);
        boolean buttonCO2Selected = buttonCO2.createAccessibilityNodeInfo().isChecked();

        Button buttonLight = root.findViewById(R.id.buttonLight);
        boolean buttonLightSelected = buttonLight.createAccessibilityNodeInfo().isChecked();

        LineChart lineChart = root.findViewById(R.id.chart);

        List<ILineDataSet> list = dataSets.stream().filter(set -> (set.getLabel().equals("°C") && buttonTempSelected)
                        || (set.getLabel().equals("φ(%)") && buttonHumiditySelected)
                        || (set.getLabel().equals("CO2(ppm)") && buttonCO2Selected)
                        || (set.getLabel().equals("E(*10)") && buttonLightSelected))
                .collect(Collectors.toList());

        LineData data = null;
        if (list.size() > 0)
            data = new LineData(list);

        // Передадим данные для графика в сам график
        lineChart.setData(data);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public String getFormattedValue(float value) {
                Date date = new Date(Float.valueOf(value).longValue());
                return new SimpleDateFormat("dd.MM").format(date);
            }
        });
        lineChart.invalidate();
    }
}