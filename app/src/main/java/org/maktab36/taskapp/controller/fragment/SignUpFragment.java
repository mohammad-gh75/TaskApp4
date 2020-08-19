package org.maktab36.taskapp.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.maktab36.taskapp.R;
import org.maktab36.taskapp.model.User;
import org.maktab36.taskapp.repository.UserRepository;


public class SignUpFragment extends Fragment {
    public static final String EXTRA_SIGN_USERNAME = "org.maktab36.taskapp.signUsername";
    public static final String EXTRA_SIGN_PASSWORD = "org.maktab36.taskapp.signPassword";
    public static final String ARG_USERNAME = "username";
    public static final String ARG_PASSWORD = "password";
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButtonSignUp;
    private String mUsername;
    private String mPassword;
    private User mUser;

    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String username,String password) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME,username);
        args.putString(ARG_PASSWORD,password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername=getArguments().getString(ARG_USERNAME);
        mPassword=getArguments().getString(ARG_PASSWORD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);
        findViews(view);
        loadInfo();
        setListeners();
        return view;
    }

    private void loadInfo() {
        mEditTextUsername.setText(mUsername);
        mEditTextPassword.setText(mPassword);
    }

    private void findViews(View view) {
        mEditTextUsername = view.findViewById(R.id.edit_text_sign_up_username);
        mEditTextPassword = view.findViewById(R.id.edit_text_sign_up_number_password);
        mButtonSignUp = view.findViewById(R.id.sign_up_button_sign_up);
    }

    private void setListeners() {
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputValidity()) {
                    if(checkInfo()) {
                        saveInfo();
                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity(),
                                R.string.toast_exist_user, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            R.string.input_wrong_format, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveInfo() {
        String username=mEditTextUsername.getText().toString();
        String password=mEditTextPassword.getText().toString();
        mUser=new User(username,password);
        UserRepository.getInstance(getActivity()).addUser(mUser);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SIGN_USERNAME,mUser.getUsername());
        intent.putExtra(EXTRA_SIGN_PASSWORD,mUser.getPassword());
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private boolean checkInputValidity() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        return !username.equals("") && !password.equals("");
    }

    private boolean checkInfo() {
        String username=mEditTextUsername.getText().toString();
        return !(UserRepository.getInstance(getActivity()).containUser(username));
    }
}