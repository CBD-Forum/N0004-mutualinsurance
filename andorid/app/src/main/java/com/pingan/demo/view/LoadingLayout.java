package com.pingan.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pingan.demo.R;

/**
 * 用于网络请求过程中的内部loading样式
 */
public class LoadingLayout extends FrameLayout {
    private View loadingLayout;
    private View errorLayout, nodataLayout;
    private TextView errorTextView, noDataErrorTextView;
    private OnClickListener mRefreshClickListener;
    private Context context;
    private int loadinglayoutId;
    private int errorLayoutId;
    private int nodataLayoutId;


    public LoadingLayout(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
            if (a != null) {
                loadinglayoutId = a.getResourceId(R.styleable.LoadingLayout_loading_layout,
                        R.layout.layout_loading);
                errorLayoutId = a.getResourceId(R.styleable.LoadingLayout_layout_net_fail,
                        R.layout.layout_net_fail);
                nodataLayoutId = a.getResourceId(R.styleable.LoadingLayout_layout_no_data_fail,
                        R.layout.layout_no_data);
            }
        }
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置重试按钮的点击监听
     *
     * @param listener
     */
    public void setRefreshClickListener(OnClickListener listener) {
        this.mRefreshClickListener = listener;
    }

    /**
     * 功能描述:创建局部刷新布局&错误布局
     */
    public void setUpPartProcessLayout() {

        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.bringToFront();
    }

    /**
     * 显示加载中转圈
     */

    public void showProcess() {
        bringToFront();
        if (loadingLayout == null) {
            LayoutInflater infalter = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            loadingLayout = infalter.inflate(loadinglayoutId, null);
            addView(loadingLayout,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
        }
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.VISIBLE);
            GifView gifView = (GifView) loadingLayout.findViewById(R.id.iv_new_loading);
            gifView.setMovieResource(R.raw.gif_loading);
            loadingLayout.bringToFront();
        }
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
        if (nodataLayout != null) {
            nodataLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏加载中的转圈显示
     */
    public void removeProcess() {
        setVisibility(View.GONE);
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示加载失败后的错误信息
     */
    public void showError() {
        if (errorLayout == null) {
            LayoutInflater infalter = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            errorLayout = infalter.inflate(errorLayoutId, null);
            errorTextView = (TextView) errorLayout.findViewById(R.id.error_text);
            if (mRefreshClickListener != null) {
                View errorImage = errorLayout.findViewById(R.id.network_error_img);
                if (errorImage != null) {
                    errorImage.setOnClickListener(mRefreshClickListener);
                }
            }
            addView(errorLayout,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
        }
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.bringToFront();
    }

    /**
     * 显示加载数据错误信息
     */
    public void showRequestError() {
        bringToFront();
        if (errorLayout == null) {
            LayoutInflater infalter = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            errorLayout = infalter.inflate(errorLayoutId, null);
            errorTextView = (TextView) errorLayout.findViewById(R.id.error_text);
            if (mRefreshClickListener != null) {
                View errorImage = errorLayout.findViewById(R.id.network_error_img);
                if (errorImage != null) {
                    errorImage.setOnClickListener(mRefreshClickListener);
                }
            }
            addView(errorLayout,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
        }
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);

        }
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.bringToFront();
    }

    /**
     * 显示无数据时的错误信息
     */
    public void showNoDataError() {
        if (nodataLayout == null) {
            LayoutInflater infalter = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            nodataLayout = infalter.inflate(nodataLayoutId, null);
            noDataErrorTextView = (TextView) nodataLayout.findViewById(R.id.error_text);
            addView(nodataLayout,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
        }
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
        nodataLayout.setVisibility(View.VISIBLE);
        nodataLayout.bringToFront();
    }

    /**
     * 更新错误提示信息
     *
     * @param str
     */
    public void updateErrorText(String str) {
        if (errorTextView != null) {
            errorTextView.setText(str);
        }
    }

    /**
     * 更新无数据时的错误提示信息
     *
     * @param str
     */
    public void updateNoDataErrorText(String str) {
        if (noDataErrorTextView != null) {
            noDataErrorTextView.setText(str);
        }
    }

    /**
     * 隐藏错误布局
     */
    public void hideErrorLayout() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
        if (nodataLayout != null) {
            nodataLayout.setVisibility(View.GONE);
        }
    }
}
