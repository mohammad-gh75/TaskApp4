package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.controller.activity.SignUpActivity;
import org.maktab36.taskapp.controller.activity.TabViewPagerActivity;
import org.maktab36.taskapp.model.User;
import org.maktab36.taskapp.repository.UserRepository;


public class LoginFragment extends Fragment {
    public static final int REQUEST_CODE_LOGIN_ACTIVITY = 2;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonSignUp;
    private User mUser;
    private UserRepository mRepository;


    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository=UserRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        findViews(view);
        if (mUser == null) {
            loadCurrentUserInfo();
        }
        setListeners();
        return view;
    }


    private void loadCurrentUserInfo() {
        mUser = mRepository.getCurrentUser();
        if (mUser != null) {
            mEditTextUsername.setText(mUser.getUsername());
            mEditTextPassword.setText(mUser.getPassword());
        }
    }

    private void findViews(View view) {
        mEditTextUsername = view.findViewById(R.id.edit_text_login_username);
        mEditTextPassword = view.findViewById(R.id.edit_text_login_number_password);
        mButtonLogin = view.findViewById(R.id.login_button_login);
        mButtonSignUp = view.findViewById(R.id.login_button_sign_up);
    }

    private void setListeners() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=mEditTextUsername.getText().toString();
                String password=mEditTextPassword.getText().toString();
                mUser=mRepository.getUser(username,password);
                if (mUser!=null) {
                    mRepository.setCurrentUser(mUser);
                    startTabViewPagerActivity();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), R.string.toast_incorrect_info, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
            }
        });
    }

    private void startSignUpActivity() {
        String username=mEditTextUsername.getText().toString();
        String password=mEditTextPassword.getText().toString();
        Intent intent = SignUpActivity.newIntent(getActivity(),username,password);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_LOGIN_ACTIVITY) {
            String username=data.getStringExtra(SignUpFragment.EXTRA_SIGN_USERNAME);
            String password=data.getStringExtra(SignUpFragment.EXTRA_SIGN_PASSWORD);
            mEditTextUsername.setText(username);
            mEditTextPassword.setText(password);
        }
    }


    private void startTabViewPagerActivity() {
        Intent intent = TabViewPagerActivity.newIntent(getActivity());
        startActivity(intent);
    }
}