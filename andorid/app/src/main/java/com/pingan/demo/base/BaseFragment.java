package com.pingan.demo.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pingan.demo.R;
import com.pingan.demo.view.LoadingLayout;
import com.pingan.http.framework.task.MarkAble;

public class BaseFragment extends Fragment implements MarkAble {
    protected ViewGroup baseLayout;
    protected ViewGroup mainLayout;
    protected LoadingLayout content_ll;

    @Override
    public String getInstanceTag() {
        return this.getClass().getSimpleName() + this.hashCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseLayout = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_base_layout, null);
        content_ll = (LoadingLayout) baseLayout.findViewById(R.id.content_ll);
        return baseLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (baseLayout != null) {
            baseLayout.setClickable(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (baseLayout != null) {
            baseLayout.setClickable(false);
        }
    }
}
