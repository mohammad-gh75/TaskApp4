package org.maktab36.taskapp.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.controller.fragment.TaskDetailFragment;
import org.maktab36.taskapp.controller.fragment.TaskListFragment;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {
    private List<Task> mTaskList;
    private Fragment mFragment;
    private UUID mUserId;

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }

    public TaskListAdapter(Fragment fragment, List<Task> taskList, UUID userId) {
        mFragment=fragment;
        mTaskList= taskList;
        mUserId=userId;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
        View view = inflater.inflate(R.layout.list_row_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = mTaskList.get(position);
        holder.bindTask(task,mUserId);
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder{
        private Task mTask;
        private TextView mTextViewTaskName;
        private TextView mTextViewTaskDate;
        private TextView mTextViewTaskIcon;
        private TextView mTextViewTaskUser;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTaskName = itemView.findViewById(R.id.text_view_task_name);
            mTextViewTaskDate = itemView.findViewById(R.id.text_view_task_date);
            mTextViewTaskIcon=itemView.findViewById(R.id.text_view_task_icon);
            mTextViewTaskUser=itemView.findViewById(R.id.text_view_task_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskDetailFragment taskDetailFragment=TaskDetailFragment.newInstance(mTask.getUUID());
                    taskDetailFragment.setTargetFragment(mFragment,
                            TaskListFragment.TASK_DETAIL_REQUEST_CODE);
                    taskDetailFragment.show(mFragment.getFragmentManager(),
                            TaskListFragment.DIALOG_FRAGMENT_TAG);
                }
            });
        }

        public void bindTask(Task task,UUID userId) {
            mTask=task;
            if(userId.equals(UserRepository.getInstance(mFragment.getActivity()).getAdmin().getUUID())){
                mTextViewTaskUser.setVisibility(View.VISIBLE);
                mTextViewTaskUser.setText(UserRepository.
                        getInstance(mFragment.getActivity()).
                        getUser(task.getUserId()).
                        getUsername());
            }
            mTextViewTaskName.setText(task.getName());
            mTextViewTaskDate.setText(task.getDate().toString());
            mTextViewTaskIcon.setText(String.valueOf(task.getName().charAt(0)));
        }
    }
}
