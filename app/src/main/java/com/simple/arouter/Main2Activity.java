package com.simple.arouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.simple.arouter_annotation.ARouter;
import com.simple.arouter_annotation.Parameter;
import com.simple.arouter_api.ParameterManager;
import com.simple.common.order.drawable.OrderDrawable;

@ARouter
public class Main2Activity extends AppCompatActivity {

    @Parameter
    String path;

    @Parameter(name = "/order/getDrawable")
    OrderDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ParameterManager.getInstance().loadParameter(this);
        Log.d("Main2Activity", "path=" + path);

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(drawable.getDrawable());
    }
}
