package org.berendeev.roma.sciencealarm.presentation.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import org.berendeev.roma.sciencealarm.R;
import org.berendeev.roma.sciencealarm.presentation.view.DataPicker;
import org.berendeev.roma.sciencealarm.presentation.view.DurationPicker;
import org.berendeev.roma.sciencealarm.presentation.view.DurationPicker.TimeSetListener;


public class PickerActivity extends AppCompatActivity {

//    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.new_alarm);
//        DataPicker dpMonth = (DataPicker) findViewById(R.id.dpMonth);
//
//        String[] months=new String[12];
//        months[0]="january";
//        months[1]="february";
//        months[2]="march";
//        months[3]="april";
//        months[4]="may";
//        months[5]="june";
//        months[6]="july";
//        months[7]="august";
//        months[8]="september";
//        months[9]="october";
//        months[10]="november";
//        months[11]="december";
//        dpMonth.setValues(months);
//    }


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm);
        startPicker();
    }

    private void startPicker() {
        TimeSetListener listener = new TimeSetListener(){
            @Override public void onTimeSet(TimePicker view, int minute, int second) {

            }
        };
        DurationPicker picker = DurationPicker.newInstance(this, listener, 0, 0);
        picker.show();
//        TimePickerDialog dialog = TimePickerDialog.new
    }
}
