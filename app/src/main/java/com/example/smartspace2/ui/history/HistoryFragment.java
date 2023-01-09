package com.example.smartspace2.ui.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.smartspace2.R;
import com.example.smartspace2.dto.RoomDto;
import com.example.smartspace2.service.NetworkService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private DatePickerDialog dateFromPicker;
    private DatePickerDialog dateToPicker;

    private String roomId;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        String id = (String) Optional.ofNullable(getArguments())
                .map(bundle -> bundle.get("id"))
                .orElse(null);

        EditText dateFrom = root.findViewById(R.id.date_from);
        dateFrom.setInputType(InputType.TYPE_NULL);
        dateFrom.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            dateFromPicker = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String monthS = (monthOfYear + 1) + "";
                        if ((monthOfYear + 1) < 10)
                            monthS = "0" + monthS;

                        String dayOfMonthS = dayOfMonth + "";
                        if (dayOfMonth < 10)
                            dayOfMonthS = "0" + dayOfMonthS;

                        dateFrom.setText(year1 + "-" + monthS + "-" + dayOfMonthS);
                    }, year, month, day);
            dateFromPicker.show();
        });

        EditText dateTo = root.findViewById(R.id.date_to);
        dateTo.setInputType(InputType.TYPE_NULL);
        dateTo.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            dateToPicker = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String monthS = (monthOfYear + 1) + "";
                        if ((monthOfYear + 1) < 10)
                            monthS = "0" + monthS;

                        String dayOfMonthS = dayOfMonth + "";
                        if (dayOfMonth < 10)
                            dayOfMonthS = "0" + dayOfMonthS;

                        dateTo.setText(year1 + "-" + monthS + "-" + dayOfMonthS);
                    }, year, month, day);
            dateToPicker.show();
        });

        Button searchBtn = root.findViewById(R.id.btn_search);
        LineChart lineChart = root.findViewById(R.id.chart);
        searchBtn.setOnClickListener(view -> {
            String start = dateFrom.getText().toString();
            String end = dateTo.getText().toString();

            Activity activity = getActivity();
            NetworkService.getInstance()
                    .getJSONApi()
                    .getHistory(Integer.parseInt(id), start, end)
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
                            RoomDto[] roomDtos = response.body();

                            ArrayList<Entry> entriesT = new ArrayList<>();
                            ArrayList<Entry> entriesH = new ArrayList<>();
                            float abscT = 1672877041918f;
                            float abscH = 1672877041918f;
                            for (RoomDto rd : roomDtos) {
                                entriesT.add(new Entry(abscT, (float) rd.getTemperature()));
                                abscT += 100000;
                            }

                            // На основании массива точек создадим первую линию с названием
                            LineDataSet datasetT = new LineDataSet(entriesT, "Температура");
                            datasetT.setDrawFilled(true);

                            for (RoomDto rd : roomDtos) {
                                entriesH.add(new Entry(abscT, (float) rd.getHumidity()));
                                abscH += 100000;
                            }
                            LineDataSet datasetH = new LineDataSet(entriesH, "Влажность");
                            datasetH.setDrawFilled(true);

                            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(datasetT);
                            //dataSets.add(datasetH);
                            LineData data = new LineData(dataSets);

                            // Передадим данные для графика в сам график
                            lineChart.setData(data);
                            lineChart.animateY(500);
                            lineChart.animateX(500);
                            lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
                                @SuppressLint("SimpleDateFormat")
                                @Override
                                public String getFormattedValue(float value) {
                                    Date date = new Date(Float.valueOf(value).longValue());
                                    return new SimpleDateFormat("dd.MM").format(date);
                                }
                            });

                            // Не забудем отправить команду на перерисовку кадра, иначе график не отобразится
                            lineChart.invalidate();
                        }

                        @Override
                        public void onFailure(Call<RoomDto[]> call, Throwable t) {
                            Toast toast = Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });


        });

        return root;
    }
}