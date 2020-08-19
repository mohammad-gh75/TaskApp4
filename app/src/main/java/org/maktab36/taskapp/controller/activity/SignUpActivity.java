package org.maktab36.taskapp.controller.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import org.maktab36.taskapp.controller.fragment.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivity {
    public static final String EXTRA_USERNAME = "org.maktab36.taskapp.username";
    public static final String EXTRA_PASSWORD = "org.maktab36.taskapp.password";

    public static Intent newIntent(Context context,String username,String password) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        String username=getIntent().getStringExtra(EXTRA_USERNAME);
        String password=getIntent().getStringExtra(EXTRA_PASSWORD);
        return SignUpFragment.newInstance(username,password);
    }
}