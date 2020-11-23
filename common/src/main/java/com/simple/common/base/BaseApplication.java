package com.simple.common.base;

import android.app.Application;
import android.util.Log;
import com.simple.common.utils.Cons;


/**
 * 项目父Application
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(Cons.TAG, "common/BaseApplication");
    }
}
