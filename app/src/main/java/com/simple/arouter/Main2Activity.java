package com.simple.arouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.simple.arouter_annotation.ARouter;
import com.simple.arouter_annotation.Parameter;
import com.simple.arouter_api.ParameterManager;
import com.simple.common.order.drawable.OrderDrawable;

import java.io.Serializable;
import java.util.ArrayList;

@ARouter
public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";
    
    @Parameter
    String path;

    @Parameter
    float floatV;

    @Parameter(name = "/order/getDrawable")
    OrderDrawable drawable;

    @Parameter
    Parcelable par;

    @Parameter(name = "ser")
    Serializable serializableParam;

    @Parameter
    ArrayList<String> arrayListString;

    @Parameter
    String[] strings;

    @Parameter
    TestParcelable[] testParcelables;

    @Parameter
    ArrayList<TestParcelable> parList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ParameterManager.getInstance().loadParameter(this);
        Log.d(TAG, "path=" + path);
        Log.d(TAG, "parcelableParam=" + par);
        Log.d(TAG, "serializableParam=" + serializableParam);
        Log.d(TAG, "floatV=" + floatV);
        Log.d(TAG, "arrayListString=" + arrayListString);
        Log.d(TAG, "parList=" + parList);

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(drawable.getDrawable());
    }
}
