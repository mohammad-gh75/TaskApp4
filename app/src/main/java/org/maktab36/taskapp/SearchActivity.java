package org.maktab36.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab36.taskapp.controller.activity.SingleFragmentActivity;
import org.maktab36.taskapp.controller.fragment.SearchFragment;

public class SearchActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,SearchActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return SearchFragment.newInstance();
    }

}