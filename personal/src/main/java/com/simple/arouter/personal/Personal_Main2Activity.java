package com.simple.arouter.personal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simple.arouter_annotation.ARouter;
import com.simple.arouter_annotation.Parameter;

@ARouter(path = "/personal/Personal_Main2Activity")
public class Personal_Main2Activity extends AppCompatActivity {

    @Parameter
    String math;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main2);
    }
}
