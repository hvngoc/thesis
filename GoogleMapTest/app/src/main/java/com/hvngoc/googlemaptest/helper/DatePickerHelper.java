package com.hvngoc.googlemaptest.helper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Hoang Van Ngoc on 03/05/2016.
 */
public class DatePickerHelper {
    private EditText editText;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    public DatePickerHelper(Context context, EditText editText){
        this.editText = editText;
        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                UpdateToTextView();
            }
        };

        datePickerDialog = new DatePickerDialog(context, dateSetListener, 2000, 1, 1);
        datePickerDialog.show();
    }

    private void UpdateToTextView(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String text = simpleDateFormat.format(calendar.getTime());
        this.editText.setText(text);
        datePickerDialog.dismiss();
    }
}
