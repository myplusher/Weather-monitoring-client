package com.example.smartspace2.ui.report;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.smartspace2.R;
import com.example.smartspace2.dto.HistoryItem;
import com.example.smartspace2.dto.ReportDto;
import com.example.smartspace2.dto.RoomHistoryDto;
import com.example.smartspace2.service.NetworkService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReportFragment extends Fragment {
    String targetPdf = "/storage/emulated/0/Download/";
    File fileOut;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report, container, false);

        String id = Optional.ofNullable(getArguments())
                .map(bundle -> bundle.getString("id"))
                .orElse(null);

        Optional.ofNullable(getArguments())
                .map(bundle -> bundle.getString("name"))
                .ifPresent(((AppCompatActivity) getActivity()).getSupportActionBar()::setSubtitle);

        TextView selectedDate = root.findViewById(R.id.selected_date);
        SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        long startDate = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(startDate);

        selectedDate.setText(viewDateFormat.format(new Date(startDate)));

        MaterialDatePicker<Long> picker = datePicker.build();
        picker.addOnPositiveButtonClickListener(selection -> {
            Date start = new Date(selection);

            String viewText = viewDateFormat.format(start);
            selectedDate.setText(viewText);
            sendData(root, id, start);
        });

        Button rangeBtn = root.findViewById(R.id.btn_date);
        rangeBtn.setOnClickListener(view -> picker.show(getParentFragmentManager(), "tag"));

        sendData(root, id, new Date());

        return root;
    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        deleteTempFile();
        super.onDestroyView();
    }

    private void openPDF(ImageView pdfView, File file) throws IOException {
        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(
                file, ParcelFileDescriptor.MODE_READ_ONLY);

        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);

        final int pageCount = pdfRenderer.getPageCount();

        int width = 0;
        int height = 0;
        List<Bitmap> bitmapList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page rendererPage = pdfRenderer.openPage(i);
            int rendererPageWidth = rendererPage.getWidth();
            if (rendererPageWidth > width)
                width = rendererPageWidth;
            int rendererPageHeight = rendererPage.getHeight();
            height += rendererPageHeight;

            Bitmap bitmap = Bitmap.createBitmap(
                    rendererPageWidth,
                    rendererPageHeight,
                    Bitmap.Config.ARGB_8888);

            rendererPage.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            bitmapList.add(bitmap);
            rendererPage.close();
        }

        if (bitmapList.size() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            int positionLeft=0;
            int positionTop=0;
            for (Bitmap b : bitmapList) {
                positionLeft = (width - b.getWidth()) / 2;
                canvas.drawBitmap(b, positionLeft, positionTop,null);
                positionTop += 10 + b.getHeight();
            }
            pdfView.setImageBitmap(bitmap);
        }
        pdfRenderer.close();
        fileDescriptor.close();
    }

    private void sendData(View root, String id, Date startDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sendDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = sendDateFormat.format(startDate);
        NetworkService.getInstance()
                .getJSONApi()
                .getReport(Integer.parseInt(id), start)
                .enqueue(new Callback<ReportDto>() {
                    @Override
                    public void onResponse(Call<ReportDto> call, Response<ReportDto> response) {
                        if (!response.isSuccessful()) {
                            switch (response.code()) {
                                case 404:
                                    Snackbar.make(root, "Неверный запрос", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Snackbar.make(root, "Ошибка на сервере", Snackbar.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Snackbar.make(root, "Непредвиденная ошибка", Snackbar.LENGTH_SHORT).show();
                                    break;
                            }
                            return;
                        }

                        ReportDto body = response.body();
                        String file = Optional.ofNullable(body)
                                .map(ReportDto::getFile)
                                .orElse(null);

                        byte[] decodedDocument = Base64.getDecoder()
                                .decode(file.getBytes(StandardCharsets.UTF_8));

                        deleteTempFile();

                        fileOut = new File(targetPdf + "temp_" + new Date().getTime() + ".pdf");

                        try (FileOutputStream stream = new FileOutputStream(fileOut)) {
                            stream.write(decodedDocument);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        ImageView pdfView = root.findViewById(R.id.pdfView);
                        try {
                            openPDF(pdfView, fileOut);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportDto> call, Throwable t) {
                        Snackbar.make(root, t.toString(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteTempFile() {
        if (fileOut != null && fileOut.exists())
            fileOut.delete();
    }
}