package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.controller.activity.TabViewPagerActivity;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;
import org.maktab36.taskapp.repository.TaskRepository;
import org.maktab36.taskapp.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskDetailFragment extends DialogFragment {
    public static final String ARG_TASK_ID = "TaskId";
    public static final String DIALOG_FRAGMENT_TAG = "Dialog";
    public static final String BUNDLE_VIEWS_ENABLED = "ViewsEnabled";
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;
    private Task mCurrentTask;

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupStates;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private Button mButtonSave;
    private Button mButtonCancel;
    private SimpleDateFormat mDateFormatter;
    private SimpleDateFormat mTimeFormatter;
    private TaskRepository mTaskRepository;
    private UserRepository mUserRepository;
    private boolean mButtonVisibility;
    private boolean mViewsEnabled;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    public static TaskDetailFragment newInstance(UUID taskId) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID,taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskRepository =TaskRepository.getInstance(getActivity());
        mUserRepository=UserRepository.getInstance(getActivity());

        UUID taskId= (UUID) getArguments().getSerializable(ARG_TASK_ID);
        if(taskId!=null) {
            mCurrentTask = mTaskRepository.get(mUserRepository.getCurrentUser().getId(),taskId);
            mButtonVisibility=false;
            mViewsEnabled=false;
        }else{
            mViewsEnabled=true;
            mButtonVisibility=true;
            mCurrentTask=new Task(mUserRepository.getCurrentUser().getId());
            mCurrentTask.setState(TaskState.TODO);
            mCurrentTask.setDate(new Date());
        }

        mDateFormatter=new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        mTimeFormatter=new SimpleDateFormat("HH:mm", Locale.US);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_task_detail, container, false);
        findViews(view);
        setButtonsVisibility(mButtonVisibility);
        loadState(savedInstanceState);
        setUI();
        setListeners();
        setViewEnabled(mViewsEnabled);
        return view;
    }

    private void findViews(View view) {
        mEditTextTitle=view.findViewById(R.id.edit_text_detail_title);
        mEditTextDescription=view.findViewById(R.id.edit_text_detail_description);
        mButtonDate=view.findViewById(R.id.button_detail_date);
        mButtonTime=view.findViewById(R.id.button_detail_time);
        mRadioGroupStates=view.findViewById(R.id.radio_group_states);
        mButtonCancel=view.findViewById(R.id.button_cancel);
        mButtonDelete=view.findViewById(R.id.button_delete);
        mButtonEdit=view.findViewById(R.id.button_edit);
        mButtonSave=view.findViewById(R.id.button_save);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_VIEWS_ENABLED,mViewsEnabled);
    }
    private void loadState(Bundle saveInstanceState){
        if(saveInstanceState!=null){
            mViewsEnabled=saveInstanceState.getBoolean(BUNDLE_VIEWS_ENABLED);
        }
    }

    private void setButtonsVisibility(boolean visibility) {
        if(mButtonVisibility){
            mButtonDelete.setVisibility(View.GONE);
            mButtonEdit.setVisibility(View.GONE);
            mButtonCancel.setVisibility(View.VISIBLE);
        }else{
            mButtonDelete.setVisibility(View.VISIBLE);
            mButtonEdit.setVisibility(View.VISIBLE);
            mButtonCancel.setVisibility(View.GONE);
        }
    }

    private void setUI() {
        mEditTextTitle.setText(mCurrentTask.getName());
        mEditTextDescription.setText(mCurrentTask.getDescription());
        mButtonDate.setText(mDateFormatter.format(mCurrentTask.getDate()));
        mButtonTime.setText(mTimeFormatter.format(mCurrentTask.getDate()));
        switch (mCurrentTask.getState()){
            case DOING:
                mRadioGroupStates.check(R.id.radio_button_state_doing);
                break;
            case DONE:
                mRadioGroupStates.check(R.id.radio_button_state_done);
                break;
            case TODO:
                mRadioGroupStates.check(R.id.radio_button_state_todo);
                break;
        }
    }
    private Task getUI(){
        Task tempTask=new Task(mUserRepository.getCurrentUser().getId());
        tempTask.setId(mCurrentTask.getId());
        tempTask.setName(mEditTextTitle.getText().toString());
        tempTask.setDescription(mEditTextDescription.getText().toString());
        switch (mRadioGroupStates.getCheckedRadioButtonId()){
            case R.id.radio_button_state_doing:
                tempTask.setState(TaskState.DOING);
                break;
            case R.id.radio_button_state_done:
                tempTask.setState(TaskState.DONE);
                break;
            case R.id.radio_button_state_todo:
                tempTask.setState(TaskState.TODO);
                break;
        }
        return tempTask;
    }
    private void setListeners() {
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTaskRepository.delete(mCurrentTask);
                setResult();
                dismiss();
            }
        });

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewsEnabled=true;
                setViewEnabled(true);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task=getUI();
                if(mButtonVisibility){
                    task.setDate(mCurrentTask.getDate());
                    mCurrentTask=task;
                    mTaskRepository.insert(mCurrentTask);
                    ((TabViewPagerActivity)getActivity()).updateFragments();
                }else{
                    task.setDate(mCurrentTask.getDate());
                    mTaskRepository.update(task);
                    setResult();
                }
                dismiss();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment=DatePickerFragment.newInstance(mCurrentTask.getDate());
                datePickerFragment.setTargetFragment(TaskDetailFragment.this,DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(),DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment=TimePickerFragment.newInstance(mCurrentTask.getDate());
                timePickerFragment.setTargetFragment(TaskDetailFragment.this,TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(),DIALOG_FRAGMENT_TAG);
            }
        });
    }

    private void setViewEnabled(boolean enabled) {
        mEditTextTitle.setEnabled(enabled);
        mEditTextDescription.setEnabled(enabled);
        mButtonDate.setEnabled(enabled);
        mButtonTime.setEnabled(enabled);
        for (int i = 0; i <mRadioGroupStates.getChildCount() ; i++) {
            mRadioGroupStates.getChildAt(i).setEnabled(enabled);
        }
    }

    private void setResult() {
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Date userSelectedDate=mCurrentTask.getDate();
        Date userSelectedTime=mCurrentTask.getDate();
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            mButtonDate.setText(mDateFormatter.format(userSelectedDate));
        }
        if (requestCode == TIME_PICKER_REQUEST_CODE) {
            userSelectedTime = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mButtonTime.setText(mTimeFormatter.format(userSelectedTime));
        }
        setTaskDate(userSelectedDate, userSelectedTime);
//        updateTask();
    }

    private void setTaskDate(Date userSelectedDate , Date userSelectedTime) {
        Calendar calendar=Calendar.getInstance();

        calendar.setTime(userSelectedTime);
        int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        calendar.setTime(userSelectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mCurrentTask.setDate(calendar.getTime());
    }
}