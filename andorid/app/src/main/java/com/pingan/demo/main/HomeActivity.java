package com.pingan.demo.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.pingan.demo.R;
import com.pingan.demo.base.BaseActivity;
import com.pingan.demo.base.BaseFragment;
import com.pingan.demo.controller.FragmentManagerControl;

public class HomeActivity extends BaseActivity implements OnCheckedChangeListener {
    public static RadioButton rightTab;
    public static RadioButton leftTab;
    public static RadioButton middleTab;
    public static RadioButton blockTab;


    private BillListFragment billListFragment;
    private PayFragment payFragment;
    private MyHomeFragment myHomeFragment;
    private BlockFragment blockFragment;
    private BaseFragment currentFragment;
    private long lastClickBackTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            if (FragmentManagerControl.getInstance().getFragmanager() != null &&
                    FragmentManagerControl.getInstance().getFragmanager().getBackStackEntryCount()
                            == 0) {
                if (System.currentTimeMillis() - lastClickBackTime > 2000) {
                    Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                    lastClickBackTime = System.currentTimeMillis();
                    return true;
                } else {
                    finish();
                    return false;
                }
            }


        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        leftTab = (RadioButton) findViewById(R.id.leftTab);
        rightTab = (RadioButton) findViewById(R.id.rightTab);
        middleTab = (RadioButton) findViewById(R.id.middleTab);
        blockTab = (RadioButton) findViewById(R.id.blockTab);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        /* 给单选按钮设置监听*/
        leftTab.setOnCheckedChangeListener(this);
        rightTab.setOnCheckedChangeListener(this);
        middleTab.setOnCheckedChangeListener(this);
        blockTab.setOnCheckedChangeListener(this);
        FragmentManagerControl.getInstance().setFragmentManager(getSupportFragmentManager());
        billListFragment = new BillListFragment();
        leftTab.setChecked(true);
    }

    /**
     * Tab按钮选中监听事件
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(getResources().getColor(R.color.color_009ee1));
        } else {
            buttonView.setTextColor(getResources().getColor(R.color.color_666666));
        }

        switch (buttonView.getId()) {
            case R.id.leftTab:
                if (isChecked) {
                    FragmentManagerControl.getInstance()
                            .switchFragment(currentFragment, billListFragment);
                    currentFragment = billListFragment;
                }
                break;
            case R.id.middleTab:
                if (isChecked) {
                    if (myHomeFragment == null) {
                        myHomeFragment = new MyHomeFragment();
                    }
                    FragmentManagerControl.getInstance()
                            .switchFragment(currentFragment, myHomeFragment);
                    currentFragment = myHomeFragment;
                }
                break;
            case R.id.rightTab:
                if (isChecked) {
                    if (payFragment == null) {
                        payFragment = new PayFragment();
                    }
                    FragmentManagerControl.getInstance()
                            .switchFragment(currentFragment, payFragment);
                    currentFragment = payFragment;
                }
                break;
            case R.id.blockTab:
                if (isChecked) {
                    if (blockFragment == null) {
                        blockFragment = new BlockFragment();
                    }
                    FragmentManagerControl.getInstance()
                            .switchFragment(currentFragment, blockFragment);
                    currentFragment = blockFragment;
                }
                break;
            default:
                break;
        }
    }
}
