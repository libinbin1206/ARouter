package com.simple.arouter_api;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 跳转时 ，用于参数的传递
 */
public class BundleManager {

    // Intent传输  携带的值，保存到这里
    private Bundle bundle = new Bundle();

    public Bundle getBundle() {
        return this.bundle;
    }

    private Call call;
    Call getCall() {
        return call;
    }
    void setCall(Call call) {
        this.call = call;
    }

    // 对外界提供，可以携带参数的方法
    public BundleManager withString(@NonNull String key, @Nullable String value) {
        bundle.putString(key, value);
        return this; // 链式调用效果 模仿开源框架
    }

    public BundleManager withBoolean(@NonNull String key, @Nullable boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleManager withInt(@NonNull String key, @Nullable int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleManager withFloat(@NonNull String key, @Nullable float value) {
        bundle.putFloat(key, value);
        return this;
    }

    public BundleManager withParcelable(@NonNull String key, @Nullable Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }


    public BundleManager withSerializable(@NonNull String key, @Nullable Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleManager withStringArrayList(@NonNull String key, @Nullable ArrayList<String> value) {
        bundle.putStringArrayList(key, value);
        return this;
    }

    public BundleManager withIntegerArrayList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public BundleManager withParcelableArrayList(@NonNull String key, @Nullable ArrayList<Parcelable> value) {
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    // 直接完成跳转
    public Object navigation(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return RouterManager.getInstance().navigation(context, this);
        }
        return null;
    }
}
