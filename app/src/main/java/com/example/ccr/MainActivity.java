package com.example.ccr;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinnerTimeSlots;
    private DatePicker datePicker;
    private Button bookNowButton, showButton;
    private EditText studentIDEditText;
    MyDatabaseHelper myDatabaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHelper = new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        spinnerTimeSlots = findViewById(R.id.spinnerTimeSlotsID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_slots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlots.setAdapter(adapter);

        datePicker = findViewById(R.id.datePickerID);
        bookNowButton = findViewById(R.id.bookNowButtonID);
        showButton = findViewById(R.id.showButtonID);
        studentIDEditText = findViewById(R.id.studentEditTextID);

        bookNowButton.setOnClickListener(this);
        showButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String selectedDate = day + "/" + month + "/" + year;
        String selectedTimeSlot = spinnerTimeSlots.getSelectedItem().toString();
        String id = studentIDEditText.getText().toString();

        if (id.equals("")) {
            Toast.makeText(MainActivity.this, "Please enter Student ID", Toast.LENGTH_SHORT).show();
        } else {
            if (v.getId() == R.id.bookNowButtonID) {
                // Check if the selected time slot and date are available
                if (!isBookingAvailable(selectedDate, selectedTimeSlot)) {
                    Toast.makeText(getApplicationContext(), "This time slot is not available on selected date", Toast.LENGTH_LONG).show();
                    return;
                }

                long rowID = myDatabaseHelper.insertData(id, selectedDate, selectedTimeSlot);
                if (rowID == -1) {
                    Toast.makeText(getApplicationContext(), "Booking is not successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Booking successful", Toast.LENGTH_LONG).show();
                }
            }

            if (v.getId() == R.id.showButtonID) {

                Cursor cursor = myDatabaseHelper.displayAllData();
                if (cursor.getCount() == 0) {
                    showData("Error ", "No data found");
                    return;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("Student ID     : " + cursor.getString(0) + "\n");
                        stringBuffer.append("Date   : " + cursor.getString(1) + "\n");
                        stringBuffer.append("Time slot    : " + cursor.getString(2) + "\n\n\n");
                    }
                    showData("Field Booking Details ", stringBuffer.toString());
                }
            }
        }
    }


    private boolean isBookingAvailable(String selectedDate, String selectedTimeSlot) {
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
        String selection = MyDatabaseHelper.COLUMN_DATE + " = ? AND " + MyDatabaseHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = {selectedDate, selectedTimeSlot};
        Cursor cursor = sqLiteDatabase.query(
                MyDatabaseHelper.TABLE_BOOKINGS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isAvailable = cursor.getCount() == 0;
        cursor.close();
        return isAvailable;
    }

    public void showData(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
}