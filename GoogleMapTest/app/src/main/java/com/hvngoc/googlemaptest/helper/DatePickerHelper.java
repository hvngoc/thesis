package com.hvngoc.googlemaptest.helper;

import android.app.FragmentManager;
import android.graphics.Color;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
/**
 * Created by Hoang Van Ngoc on 03/05/2016.
 */
public class DatePickerHelper{
    private EditText editText;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    public DatePickerHelper(FragmentManager fragmentManager, EditText editText){
        this.editText = editText;
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                UpdateToTextView();
            }
        }, 2000, 1, 1);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(true);
        datePickerDialog.setAccentColor(Color.parseColor("#088A85"));
        datePickerDialog.show(fragmentManager, "Datepickerdialog");
    }

    private void UpdateToTextView(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String text = simpleDateFormat.format(calendar.getTime());
        this.editText.setText(text);
        datePickerDialog.dismiss();
    }
}
