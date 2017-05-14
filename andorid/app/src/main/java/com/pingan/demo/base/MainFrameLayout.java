package com.pingan.demo.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created by guolidong752 on 16/4/25.
 */
public class MainFrameLayout extends FrameLayout {

    public MainFrameLayout(Context context) {
        super(context);
        init();
    }

    public MainFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}
