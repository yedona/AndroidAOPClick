package com.yedona.androidaopclick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.yedona.aopclick.Except;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            @Except
            public void onClick(View v) {

                Log.d("yedona", "onClick:设置OnClick方式11111111111111111111111");

            }
        });


        findViewById(R.id.btn2).setOnClickListener(v -> {
            Log.d("yedona", "onClick:lambda表达式方式11111111111111111111");

        });

    }

    @Except
    public void btnOnClick(View view) {
        Log.d("yedona", "onClick:布局中设置11111111111111111111111");

    }

    @OnClick(R.id.btn3)
    public void onViewClicked() {

        Log.d("yedona", "butterknife注解方式111111111111111111111111");
    }
}