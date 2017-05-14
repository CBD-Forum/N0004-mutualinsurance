package com.pingan.demo.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingan.demo.R;
import com.pingan.demo.view.LoadingLayout;
import com.pingan.http.framework.task.MarkAble;

/**
 *
 */
public class BaseActivity extends FragmentActivity implements MarkAble {
    protected TextView titleTextView;
    protected FrameLayout returnBtnLayout;
    protected Button returnBtn;
    protected TextView mRightTextView;
    protected FrameLayout mContent;
    protected LoadingLayout content_ll;
    protected LayoutInflater inflater;
    private MainFrameLayout mMainView;
    private int mStatusHeight = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void setContentView(int layoutResID) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_base);
        baseInitView();
        inflater = LayoutInflater.from(this);
        inflater.inflate(layoutResID, mContent);
    }

    public int getStatusBarHeight() {
        if (mStatusHeight <= 0) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return mStatusHeight;
    }

    private void baseInitView() {
        mMainView = (MainFrameLayout) findViewById(R.id.mainView);
        mContent = (FrameLayout) findViewById(R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            RelativeLayout mTitleLayout = (RelativeLayout) findViewById(R.id.title);
            mTitleLayout.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        titleTextView = (TextView) findViewById(R.id.common_title_middle_text);
        titleTextView.setText("拉手互助平台");
        returnBtnLayout = (FrameLayout) findViewById(R.id.common_title_left_btn_layout);
        returnBtn = (Button) findViewById(R.id.common_title_left_btn);
        mRightTextView = (TextView) findViewById(R.id.common_title_right_text);
        returnBtnLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (returnBtn.getVisibility() == View.VISIBLE) {
                        returnBtn.performClick();
                    }
                }
                return true;
            }
        });
        content_ll = (LoadingLayout) findViewById(R.id.content_ll);
    }

    @Override
    public String getInstanceTag() {
        return this.getClass().getSimpleName() + this.hashCode();
    }
}
