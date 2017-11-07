package com.jsbridge.android.androidjsbridge;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jsbridge.android.androidjsbridge.HeadlinesFragment;

public class FragmentManageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

//        HeadLinesFragment firstFragment = new HeadlinesFragment();

    }

}
