package org.maktab36.taskapp.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.controller.activity.TabViewPagerActivity;
import org.maktab36.taskapp.controller.fragment.TaskDetailFragment;
import org.maktab36.taskapp.controller.fragment.TaskListFragment;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.User;
import org.maktab36.taskapp.repository.TaskRepository;
import org.maktab36.taskapp.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {
    private List<User> mUserList;
    private Fragment mFragment;

    public void setUserList(List<User> userList) {
        mUserList=userList;
    }

    public UserListAdapter(Fragment fragment, List<User> userList) {
        mFragment=fragment;
        mUserList=userList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
        View view = inflater.inflate(R.layout.list_row_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user=mUserList.get(position);
        holder.bindUser(user/*,mUserId*/);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        private User mUser;
        private TextView mTextViewUsername;
        private TextView mTextViewUserMembershipDate;
        private TextView mTextViewUserTaskNumber;
        private ImageView mImageViewDeleteUser;


        public UserHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewUsername = itemView.findViewById(R.id.text_view_admin_panel_username);
            mTextViewUserMembershipDate = itemView.findViewById(R.id.text_view_admin_panel_date);
            mTextViewUserTaskNumber=itemView.findViewById(R.id.text_view_admin_panel_task_number);
            mImageViewDeleteUser=itemView.findViewById(R.id.image_view_delete_user);

            mImageViewDeleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUserList.remove(mUser);
                    UserRepository.getInstance(mFragment.getActivity()).deleteUser(mUser);
                    TaskRepository.getInstance(mFragment.getActivity()).deleteAll(mUser.getId());
                    notifyDataSetChanged();

                }
            });
        }

        public void bindUser(User user) {
            mUser=user;
            if(user.equals(UserRepository.getInstance(mFragment.getActivity()).getAdmin())){
                mImageViewDeleteUser.setVisibility(View.INVISIBLE);
            }
            mTextViewUsername.setText(user.getUsername());
            mTextViewUserMembershipDate.setText(user.getMembershipDate().toString());
            int taskNumber=TaskRepository
                    .getInstance(mFragment.getActivity())
                    .getNumberOfUserTasks(user.getId());
            mTextViewUserTaskNumber.setText(String.valueOf(taskNumber));
        }
    }
}
