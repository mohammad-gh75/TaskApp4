package org.maktab36.taskapp.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.model.User;
import org.maktab36.taskapp.repository.UserRepository;
import org.maktab36.taskapp.util.TaskListAdapter;
import org.maktab36.taskapp.util.UserListAdapter;

import java.util.List;


public class AdminPanelFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private List<User> mUsers;


    public AdminPanelFragment() {
        // Required empty public constructor
    }


    public static AdminPanelFragment newInstance() {
        AdminPanelFragment fragment = new AdminPanelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsers=UserRepository.getInstance(getActivity()).getUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_admin_panel, container, false);
        findViews(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_users);
    }

    public void updateUI() {
        mUsers=UserRepository.getInstance(getActivity()).getUsers();
        if(mAdapter==null){
            mAdapter = new UserListAdapter(this,mUsers);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setUserList(mUsers);
            mAdapter.notifyDataSetChanged();
        }
    }
}