package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.repository.TaskRepository;
import org.maktab36.taskapp.repository.UserRepository;
import org.maktab36.taskapp.util.TaskSearchedListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment {
    //    public static final String ARG_TASK_ID = "TaskId";
    public static final String DIALOG_FRAGMENT_TAG = "Dialog";
    //    public static final String BUNDLE_VIEWS_ENABLED = "ViewsEnabled";
    public static final int DATE_PICKER_FROM_REQUEST_CODE = 0;
    public static final int DATE_PICKER_TO_REQUEST_CODE = 1;
    public static final int TIME_PICKER_FROM_REQUEST_CODE = 2;
    public static final int TIME_PICKER_TO_REQUEST_CODE = 3;
    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonDateFrom;
    private Button mButtonDateTo;
    private Button mButtonTimeFrom;
    private Button mButtonTimeTo;
    private Button mButtonSearch;
    private RecyclerView mRecyclerView;
    private TaskSearchedListAdapter mAdapter;
    private List<Task> mTasks;
    private SimpleDateFormat mDateFormatter;
    private SimpleDateFormat mTimeFormatter;
    private Date mDateFrom;
    private Date mDateTo;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        mTimeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        mTasks=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.set(1970,0,1,0,0,0);
        mDateFrom=calendar.getTime();
        calendar.set(2100,11,31,23,59,59);
        mDateTo=calendar.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        setListeners();
        updateUI();
        return view;
    }

    private void findViews(View view) {
        mEditTextTitle = view.findViewById(R.id.edit_text_search_title);
        mEditTextDescription = view.findViewById(R.id.edit_text_search_description);
        mButtonDateFrom = view.findViewById(R.id.button_search_date_from);
        mButtonDateTo = view.findViewById(R.id.button_search_date_to);
        mButtonTimeFrom = view.findViewById(R.id.button_search_time_from);
        mButtonTimeTo = view.findViewById(R.id.button_search_time_to);
        mRecyclerView = view.findViewById(R.id.recycler_view_search_tasks);
        mButtonSearch = view.findViewById(R.id.button_search);
    }

    private void setListeners() {
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] UIs=getUI();
                mTasks=TaskRepository.getInstance(getActivity()).searchTasks(UserRepository
                                .getInstance(getActivity())
                                .getCurrentUser()
                                .getUUID()
                        ,UIs[0]
                        ,UIs[1]
                        ,mDateFrom
                        ,mDateTo);
                updateUI();
            }
        });

        mButtonDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                datePickerFragment.setTargetFragment(SearchFragment.this, DATE_PICKER_FROM_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                datePickerFragment.setTargetFragment(SearchFragment.this, DATE_PICKER_TO_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new Date());
                timePickerFragment.setTargetFragment(SearchFragment.this, TIME_PICKER_FROM_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        });
        mButtonTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new Date());
                timePickerFragment.setTargetFragment(SearchFragment.this, TIME_PICKER_TO_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        });
    }

    private String[] getUI(){
        String[] texts=new String[2];
        texts[0]=mEditTextTitle.getText().toString();
        texts[1]=mEditTextDescription.getText().toString();
        return texts;
    }

    public void updateUI() {
        if(mAdapter==null){
            mAdapter = new TaskSearchedListAdapter(this,mTasks);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setTaskList(mTasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Date userSelectedDateFrom = mDateFrom;
        Date userSelectedDateTo = mDateTo;
        Date userSelectedTimeFrom = mDateFrom;
        Date userSelectedTimeTo = mDateTo;
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == DATE_PICKER_FROM_REQUEST_CODE) {
            userSelectedDateFrom = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            mButtonDateFrom.setText(mDateFormatter.format(userSelectedDateFrom));
        }
        if (requestCode == DATE_PICKER_TO_REQUEST_CODE) {
            userSelectedDateTo = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            mButtonDateTo.setText(mDateFormatter.format(userSelectedDateTo));
        }
        if (requestCode == TIME_PICKER_FROM_REQUEST_CODE) {
            userSelectedTimeFrom = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mButtonTimeFrom.setText(mTimeFormatter.format(userSelectedTimeFrom));
        }
        if (requestCode == TIME_PICKER_TO_REQUEST_CODE) {
            userSelectedTimeTo = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            mButtonTimeTo.setText(mTimeFormatter.format(userSelectedTimeTo));
        }
        setDate(userSelectedDateFrom, userSelectedDateTo, userSelectedTimeFrom, userSelectedTimeTo);
    }

    private void setDate(Date userSelectedDateFrom, Date userSelectedDateTo,
                         Date userSelectedTimeFrom, Date userSelectedTimeTo) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(userSelectedTimeFrom);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        calendar.setTime(userSelectedDateFrom);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mDateFrom = calendar.getTime();

        calendar.setTime(userSelectedTimeTo);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        calendar.setTime(userSelectedDateTo);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mDateTo = calendar.getTime();
    }
}