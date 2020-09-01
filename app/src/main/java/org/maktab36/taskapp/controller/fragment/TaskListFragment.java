package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;
import org.maktab36.taskapp.repository.TaskRepository;
import org.maktab36.taskapp.repository.UserRepository;
import org.maktab36.taskapp.util.TaskListAdapter;

import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final int TASK_DETAIL_REQUEST_CODE = 0;
    public static final String DIALOG_FRAGMENT_TAG = "Dialog";
    public static final String ARG_TASK_STATE = "taskState";
    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;
    private List<Task> mTasks;
    private ConstraintLayout mEmptyListLayout;
    private TaskState mTaskState;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(TaskState state) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_STATE, state.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskState = TaskState.valueOf(getArguments().getString(ARG_TASK_STATE));

        mTasks = getTaskList(UserRepository.getInstance(getActivity()).getCurrentUser().getUUID());
    }

    private List<Task> getTaskList(UUID userId) {
        switch (mTaskState) {
            case DOING:
                return TaskRepository.getInstance(getActivity()).getDoingTasks(userId);
            case DONE:
                return TaskRepository.getInstance(getActivity()).getDoneTasks(userId);
            case TODO:
                return TaskRepository.getInstance(getActivity()).getToDoTasks(userId);
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findViews(view);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        updateUI();
//        setListener();
        changeLayout();
        return view;
    }

    private void changeLayout() {
        if (mTasks.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyListLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyListLayout.setVisibility(View.GONE);
        }
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_tasks);
        mEmptyListLayout = view.findViewById(R.id.empty_list_layout);
    }

    /*private void setListener(){
    }*/

    public void updateUI() {
        mTasks=getTaskList(UserRepository.getInstance(getActivity()).getCurrentUser().getUUID());
        if(mAdapter==null){
            mAdapter = new TaskListAdapter(this, mTasks,
                    UserRepository.getInstance(getActivity()).getCurrentUser().getUUID());
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setTaskList(mTasks);
            mAdapter.notifyDataSetChanged();
        }
        changeLayout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        for (Fragment fragment:getActivity().getSupportFragmentManager().getFragments()) {
            if(fragment instanceof TaskListFragment){
                TaskListFragment listFragment= (TaskListFragment) fragment;
                listFragment.updateUI();
            }
        }
    }

}