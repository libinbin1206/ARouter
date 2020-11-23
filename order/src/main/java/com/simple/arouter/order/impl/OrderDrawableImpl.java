package com.simple.arouter.order.impl;

import com.simple.arouter_annotation.ARouter;
import com.simple.common.order.drawable.OrderDrawable;
import com.simple.arouter.order.R;


// order 自己决定 自己的暴漏
@ARouter(path = "/order/getDrawable")
public class OrderDrawableImpl implements OrderDrawable {
    @Override
    public int getDrawable() {
        return R.drawable.ic_launcher_background;
    }
}
