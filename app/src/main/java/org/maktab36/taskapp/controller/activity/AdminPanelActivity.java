package org.maktab36.taskapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab36.taskapp.controller.activity.SingleFragmentActivity;
import org.maktab36.taskapp.controller.fragment.AdminPanelFragment;

public class AdminPanelActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,AdminPanelActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return AdminPanelFragment.newInstance();
    }
}