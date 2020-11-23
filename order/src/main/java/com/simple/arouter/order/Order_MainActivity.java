package com.simple.arouter.order;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.simple.arouter_annotation.ARouter;
import com.simple.common.utils.Cons;

@ARouter
public class Order_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);

        Log.e(Cons.TAG, "order/Order_MainActivity");
    }

    public void jumpApp(View view) {
        Toast.makeText(this, "路由还没有写好呢，别猴急...", Toast.LENGTH_SHORT).show();
    }

    public void jumpPersonal(View view) {
        Toast.makeText(this, "路由还没有写好呢，别猴急...", Toast.LENGTH_SHORT).show();
    }
}
