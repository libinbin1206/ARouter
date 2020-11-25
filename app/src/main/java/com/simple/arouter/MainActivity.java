package com.simple.arouter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.simple.arouter.constant.App;
import com.simple.arouter_annotation.ARouter;
import com.simple.arouter_api.RouterManager;
import com.simple.arouter.order.Order_MainActivity;

import java.util.ArrayList;

@ARouter
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.isRelease) {
            Log.e(TAG, "当前为：集成化模式，除app可运行，其他子模块都是Android Library");
        } else {
            Log.e(TAG, "当前为：组件化模式，app/order/personal子模块都可独立运行");
        }
        Intent intent = new Intent();

    }

    public void jumpOrder(View view) {
        Intent intent = new Intent(this, Order_MainActivity.class);
        intent.putExtra("name", "derry");
        startActivity(intent);
    }

    public void jumpPersonal(View view) {

        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<Parcelable> parcelables = new ArrayList<>();
        parcelables.add(new TestParcelable());
        strings.add("分手大师");
        RouterManager.getInstance()
                .build(App.Main2Activity)
                .withString("path", "nimade")
                .withSerializable("ser", new TestSerializable())
                .withParcelable("par", new TestParcelable())
                .withFloat("floatV", 1.0f)
                .withStringArrayList("arrayListString", strings)
                .withParcelableArrayList("parList", parcelables)
                .navigation(this);
    }
}
