package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import org.maktab36.taskapp.R;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    public static final String ARG_TIME = "currentTime";
    public static final String EXTRA_USER_SELECTED_TIME = "org.maktab36.taskapp.userSelectedTime";
    private Date mCurrentTime;
    private TimePicker mTimePicker;


    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date currentTime) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME,currentTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentTime= (Date) getArguments().getSerializable(ARG_TIME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view=inflater.inflate(R.layout.fragment_time_picker,null);
        findViews(view);
        initTimePicker();
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date timePicked=getSelectedTimeFromTimePicker();
                        setResult(timePicked);
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create();
    }

    private void findViews(View view) {
        mTimePicker=view.findViewById(R.id.time_picker_task);
    }

    private void initTimePicker() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mCurrentTime);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minute);
    }

    private Date getSelectedTimeFromTimePicker() {
        int hourOfDay = mTimePicker.getCurrentHour();
        int minute = mTimePicker.getCurrentMinute();

        GregorianCalendar gregorianCalendar =
                new GregorianCalendar(0,0,0,hourOfDay,minute);
        return gregorianCalendar.getTime();
    }

    private void setResult(Date timePicked) {
        Fragment fragment=getTargetFragment();
        Intent intent=new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_TIME,timePicked);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }
}